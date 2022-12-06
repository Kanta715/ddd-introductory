## 1. リポジトリとは
ソフトウェア上にドメインの概念を表現しただけでは、アプリケーションとして成り立たせることは難しい。
プログラムが実行される過程でメモリ上に展開されたデータはプログラムが終了すると消えてなくなる。
オブジェクトを繰り返し利用するには、何らかのデータストアにオブジェクトのデータを永続化し、再構築する必要がある。
リポジトリはデータを永続化し再構築するといった処理を抽象的に扱うためのオブジェクトである。

オブジェクトのインスタンスを保存したい時は直接データストアに書き込む処理を実行するのではなく、リポジトリにインスタンスの永続化を依頼する。
また永続化したデータからインスタンスを再構築したい時にも、リポジトリにデータの再構築を依頼する。

データの永続化と再構築を直接行うのではなく、リポジトリを経由して行うことでソフトウェアの柔軟性が向上する。

## 2. リポジトリの責務
リポジトリの責務はドメインオブジェクトの永続化や再構築を行うこと。

[値オブジェクトやエンティティと共にユースケースを組み立てる](https://github.com/Kanta715/ddd-introductory/blob/main/src/main/scala/Knowledge/DomainService/DomainService.md#%E5%80%A4%E3%82%AA%E3%83%96%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88%E3%82%84%E3%82%A8%E3%83%B3%E3%83%86%E3%82%A3%E3%83%86%E3%82%A3%E3%81%A8%E5%85%B1%E3%81%AB%E3%83%A6%E3%83%BC%E3%82%B9%E3%82%B1%E3%83%BC%E3%82%B9%E3%82%92%E7%B5%84%E3%81%BF%E7%AB%8B%E3%81%A6%E3%82%8B) を見るとわかるようにドメインサービスにデータストアの処理を直接書くと、ビジネスロジックとは別の理由でドメインオブジェクトを改修する恐れなどがあり、柔軟性が乏しい。

ドメインサービスはリポジトリを利用することで永続化などの処理を抽象化でき、柔軟性が向上する。
```Scala
// 値オブジェクト
case class UserId  (v: Int)
case class UserName(v: String)

// エンティティ
case class User(
  id:   Option[UserId] = None,
  name: UserName
)

// リポジトリ
object UserRepository {

  private val EXISTS_USERS: Seq[User] = Seq(
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
}

// --------------------------------------------------------------

val name: String = "Pun"

val newUser: User    = User(None, UserName(name))
val exists:  Boolean = userService.checkDuplicatesAtName(newUser)

exists match {
  case true  =>
    println("ユーザー名が重複しています")
  case false =>
    val user: User = userService.create(newUser)
    println(
      s"""
         |ユーザーを作成しました
         |ユーザーID: ${user.id.get.v}
         |ユーザー名: ${user.name.v}
         |""".stripMargin)
}

// ユーザーを作成しました
// ユーザーID: 4
// ユーザー名: Pun
```
このようにすることで、DB を MySQL から NoSQL に変更するといったことが起きても、NoSQL 用のリポジトリを用意して差し替えるだけでよくなる。
