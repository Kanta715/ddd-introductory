## 1. ドメインサービスとは
システムには値オブジェクトやエンティティに記述すると不自然になってしまう振る舞いが存在する。
そういった不自然さを解消するためのオブジェクトがドメインサービスである。

### 1-2. サービスとは
「サービス」という言葉は様々な場面で使われるため明確な意味を掴みづらい。

ドメイン駆動設計における「サービス」はクライアントのために何かを行うオブジェクトのことを指す。

注意点として、ドメイン駆動設計には2種類のサービスがあり、ドメインのためのサービスとアプリケーションのためのサービスである。
ここでは、ドメインのためのサービスであるドメインサービスについて学習する。

## 2. 不自然な振る舞いを確認する
まずはシステムにおける不自然な振る舞いを見ていく。

あるシステムにおいては、ユーザー名の重複を許可しないことはありえる。このルールはドメインにおけるルールであるためドメインオブジェクトの振る舞いとして定義されるべきだが、 こういった振る舞い具体的にどのオブジェクトに定義されるべきか。

### 2-1. エンティティに定義してみる
```Scala
case class User(name: String) {

  // エンティティに定義
  def exists(user: User): Boolean = ???
}
```
このオブジェクトを見ると違和感は内容に見えるが、これは不自然さを生み出す原因になる。

```Scala
val user: User = User("Kan")

user.exists(user) match {
  case true  => println("NG: 重複しています")
  case false => println("OK: 重複していません")
}
// NG: 重複しています
/**
 * 自分で自分をチェックすることがあり得る
 * そして自分をチェックした時に、真、偽のどちらで返すべきなのか開発者を混乱させる
 */
```

### 2-2. 不自然さを解消するドメインサービス
ドメインサービスは値オブジェクトやエンティティと異なり、自身の振る舞いを変更するようなインスタンス特有の状態を持たないオブジェクト。（シングルトン）
```Scala
object UserService {

  private val EXISTS_USERS: Seq[User] = Seq(
    User("Pin"),
    User("Pon"),
    User("Pan")
  )

  def exists(user: User): Boolean =
    EXISTS_USERS.exists(_.name == user.name)
}

// -----------------------------------------------

val user: User = User("Kan")

UserService.exists(user) match {
  case true  => println("NG: 重複しています")
  case false => println("OK: 重複していません")
}
// OK: 重複していません
```
ドメインサービスとして定義することにより、自身をチェックすることがなくなり、開発者を混乱させる要素がなくなる。

## ドメインサービス濫用の行き着く先
値オブジェクトやエンティティに定義すると不自然な振る舞いはドメインサービスに定義する。ここで重要なのは「不親善な振る舞い」に限定すること。

ドメインサービスは記述しようと思えば、何でも記述できてしまう。例えば、ユーザー名を変更する振る舞いをドメインサービスに記述する。
```Scala
case class User(var name: String)

object UserService {

  def chageName(user: User, name: String): User = {
    user.name= name
    user
  }
}
```
このようにドメインサービスに全ての振る舞いを記述するとエンティティにはメンバ変数だけが残る。
エンティティがメンバ変数だけになると、そのドメインオブジェクトがどのようなルールや振る舞いがあるのか見ただけではわからなくなる。
これはオブジェクト指向設計のデータと振る舞いをまとめるという基本的な戦略の反することになる。
そのため、可能な限りドメインサービスを避けて、値オブジェクトやエンティティに記述すると不自然になるものだけに限定する。

## 値オブジェクトやエンティティと共にユースケースを組み立てる
ここではユーザー作成をするユースケースを組み立てる。

```Scala
// 値オブジェクト
case class UserId(v: Int)

case class UserName(v: String)

// ------------------------------------------

// エンティティ
case class User(id: UserId, name: UserName)

// ------------------------------------------

// ドメインサービス
class UserService {

  def create(name: String): Unit = {

    val userName: UserName = UserName(name)

    val count:  Int = ??? // sql.execute("SELECT COUNT(id) FROM user;"): 適当なSQLです
    val nextId: Int = count + 1

    val userId: UserId = UserId(nextId)

    val exists: Boolean = ??? // sql.execute("SELECT EXISTS (SELECT * FROM user WHERE id = $nextId")

    if (!exists) {
      // seq.execute("INSERT INTO user (id, name) VALUES ($nextId, $userName)")
      println("作成しました")
    } else {
      println("作成に失敗しました")
    }
  }
}
```
ここではユーザー作成をデータベースに問い合わせながら行っている。このコードは処理は正しく動作するが柔軟性に乏しい。

ドメインモデルを表現するドメインオブジェクトにデータストアの記述が多くある。
MySQL から NoSQL へ移行する場合など、これでは、ドメインとは関係のない理由でドメインサービスを改修しなければならない。

この問題はリポジトリを学習して問題解決をする。

