## 1. アプリケーションサービスとは
アプリケーションサービスはユースケースを実現するオブジェクトを指す。

例えば、ユーザー登録の必要なシステムにおいて、ユーザー機能を実現するには「ユーザーを登録する」ユースケースや「ユーザー情報を更新する」ユースケースが必要になる。
アプリケーションサービスは、これらの振る舞いをドメインオブジェクトを組み合わせて実行するスクリプトのようなものになる。

## 2. ユースケースを組み立てる
ここではユーザー機能の CRUD をユースケースとして実装する。

### 2-1. ドメインオブジェクトを準備
```Scala
// 値オブジェクト
case class UserId  (v: Int)
case class UserName(v: String)

// エンティティ
case class User(
  id:   Option[UserId] = None,
  name: UserName
) {
  def changeName(name: String): User =
    this.copy(id, name)
}

// ドメインサービス
object UserService {

  private val userRepository = UserRepository

  def checkDuplicatesAtName(user: User): Boolean =
    userRepository.checkDuplicatesAtName(user)
}

// リポジトリ
object UserRepository {

  private var EXISTS_USERS: Seq[User] = Seq(
    User(Some(UserId(1)), UserName("Pin")),
    User(Some(UserId(2)), UserName("Pon")),
    User(Some(UserId(3)), UserName("Pan"))
  )

  // 名前での重複チェック
  def checkDuplicatesAtName(user: User): Boolean =
  // sql.execute("SELECT EXISTS (SELECT * FROM user WHERE id = $user.id")
    EXISTS_USERS.exists(_.name == user.name)

  // ユーザー作成
  def create(user: User): User =
  // seq.execute("INSERT INTO user (name) VALUES ($user.name)")
    user.copy(Some(UserId(4)), user.name)

  // ユーザー取得
  def find(id: UserId): Option[User] =
  // sql.execute("SELECT * FROM user WHERE id = $id;")
    EXISTS_USERS.find(_.id.exists(_ == id))

  // 更新
  def updateOrCreate(user: User): User = {
    val userOpt = EXISTS_USERS.find(_.id == user.id)
    userOpt match {
      case Some(value) =>
        // seq.execute("UPDATE ...")
        EXISTS_USERS = EXISTS_USERS.filterNot(_.id == value.id) :+ user
        user
      case None =>
        // seq.execute("INSERT INTO user (name) VALUES ($user.name)")
        EXISTS_USERS = EXISTS_USERS :+ user
        user
    }
  }

  // 削除
  def delete(id: UserId): Option[User] = {
    val userOpt = EXISTS_USERS.find(_.id.exists(_ == id))
    userOpt match {
      case Some(user) =>
        EXISTS_USERS = EXISTS_USERS.filterNot(_ == user)
        userOpt
      case _ => None
    }
  }
}
```

### 2-3. CRUDを作成
#### 2-3-1. 作成処理
作成処理は UserApplicationService.register を使っている。
ここではユーザー名を引数に取り、既存のユーザー名と重複していないか確認してから作成をしている。

```Scala
// アプリケーションサービス
object UserApplicationService {

  private val userRepository = UserRepository
  private val userService    = UserService

  // 登録用メソッド
  def register(name: String): Unit = {
    val userName: UserName = UserName(name)
    val user: User = User(None, userName)

    val exists: Boolean = userService.checkDuplicatesAtName(user)
    exists match {
      case true =>
        userRepository.create(user)
        println("ユーザーを作成しました")
      case false =>
        println(
          """
            |ユーザー名が重複しています
            |ユーザー名を変更して再度登録してください
            |""".stripMargin)
    }
  }
}
```

#### 2-3-2. 取得処理
取得処理では get メソッドを使っている。

ここでは単純に識別子である `UserId` でデータストアから取得している。`UserId` が一致するものが無い場合もあるため返り値は `Option[_]` になっている。

##### 2-3-2-1. DTO(Data Transfer Object)
DTO とはデータ転送用オブジェクトのことを言う。

get の返り値は `Option[User]` ではなく `Option[UserData]` になっている。ここで DTO を使っている理由として、アプリケーションサービス以外からドメインオブジェクトの操作を行わせないという狙いがある。

仮に `User` を返り値とした場合 `changeName` メソッドを使って名前を変更して画面に描画するといったことも可能になる。そういったことを考慮して必要なデータだけを持つ DTO を作成し、返り値としている。


```Scala
  // 取得用メソッド
  /**
   * User を返すと changeName メソッドが生えているためアプリケーションサービス以外でドメインオブジェクトの操作が可能になる
   * それを防ぐために、必要なデータだけを持った UserData を返すようにしている
   */
  def get(id: UserId): Option[UserData] = {
    val userOpt = userRepository.find(id)
    userOpt.map(UserData.build(_))
  }
```

### 2-3-3. 更新処理
更新処理では update メソッドを使っている。
```Scala
  // 更新用メソッド
  // 既存のものがない場合は登録
  def update(user: User): User =
    userRepository.updateOrCreate(user)
```

部分的に更新を行いたい場合は、コマンドオブジェクトを用いることもあるらしい。

識別子以外は初期値を設定し、初期値以外であれば変更するといったもの。
```Scala
case class UserUpdateCommand(id: Int, name: String = "")

// Some(_) の場合は更新
case class UserUpdateCommand(id: Int, name: Option[String] = None)
```

### 2-3-4. 削除処理
削除処理は delete メソッドを使う。

識別子が一致したものを削除する単純な処理。
```Scala
  // 削除用メソッド
  def delete(id: UserId): Unit = {
    val userOpt = userRepository.delete(id)
    userOpt.isDefined match {
      case true  => println("ユーザーを削除しました")
      case false => println("ユーザーが見つかりませんでした")
    }
  }
```

## 3.ドメインのルールの流出
アプリケーションサービスはあくまでもドメインオブジェクトのタスク調整に徹する。
アプリケーションサービスにドメインのルールが記述されると同じようなコードがシステム内に点在し、改修が大変になる。

```Scala
  // 登録用メソッド
  def register(name: String): Unit = {
    val userName: UserName = UserName(name)
    val user:     User     = User(None, userName)

    val exists: Boolean = userService.checkDuplicatesAtName(user)
    exists match {
      case true =>
        println(
          """
            |ユーザー名が重複しています
            |ユーザー名を変更して再度登録してください
            |""".stripMargin)
      case false =>
        userRepository.create(user)
        println("ユーザーを作成しました")
    }
  }
  // アプリケーションサービスにドメインルールが流出しないようにする
  // ドメインルールの変更時に改修の幅が大きくなる
  // def register(name: String): Unit = {
  //    val userName: UserName = UserName(name)
  //
  //    val userOpt: Option[User] = userService.findAtName(userName)
  //    userOpt.match match { <- ドメインのルールが変わると全ての記述を改修する必要がある
  //      case true =>
  //        println(
  //          """
  //            |ユーザー名が重複しています
  //            |ユーザー名を変更して再度登録してください
  //            |""".stripMargin)
  //      case false =>
  //        userRepository.create(userName)
  //        println("ユーザーを作成しました")
  //    }
  //  }
```

## 4. アプリケーションサービスと凝集度
プログラムには凝集度という考え方がある。凝集度はモジュールの責任範囲がどれだけ集中しているかを測る尺度のことを言う。凝集度を高めると、モジュールが1つの事柄に集中することになり、堅牢性・信頼性・再利用性・可読性の観点から好ましいとされる。

凝集度を測る方法に LCOM という計算式があるらしく、端的には全てのインスタンス変数は全てのメソッドで使われるべき、というものである。

ここでは、既に宣言した `UserApplicationService` を改修して凝集度を高めてみる。

`UserApplicationService` では、インスタンス変数に `userRepository` と `userService` がある。
`userService` は `get` メソッドのみで使われており、凝集度が高いクラスとは現時点で言えない。
以下のように `CreateUserApplicationService` と `GetUserApplicationService` を作成し、凝集度の高いアプリケーションサービスを作成する。
```Scala
object CreateUserApplicationService {

  private val userRepository = UserRepository
  private val userService    = UserService

  // 登録用メソッド
  def register(name: String): Unit = {
    val userName: UserName = UserName(name)
    val user:     User     = User(None, userName)

    val exists: Boolean = userService.checkDuplicatesAtName(user)
    exists match {
      case true =>
        println(
          """
            |ユーザー名が重複しています
            |ユーザー名を変更して再度登録してください
            |""".stripMargin)
      case false =>
        userRepository.create(user)
        println("ユーザーを作成しました")
    }
  }
}

// ------------------------------------------

// UserService がなくなったことで凝集度が高まり、堅牢性・信頼性・再利用性・可読性が上がる
object GetUserApplicationService {

  private val userRepository = UserRepository

  // 取得用メソッド
  def get(id: UserId): Option[UserData] = {
    val userOpt = userRepository.find(id)
    userOpt.map(UserData.build(_))
  }
}
```