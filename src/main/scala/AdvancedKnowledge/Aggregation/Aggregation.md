## 1. 集約とは
オブジェクト指向プログラミングでは複数のオブジェクトがまとめられ、ひとつの意味を持ったオブジェクトが構築される。こうしたオブジェクトのグループには維持されるべき不変条件がある。

集約は不変条件を維持する単位として切り出され、オブジェクトの操作に秩序をもたらす。

集約には境界とルートが存在する
- 境界：集約に何が含まれるかを定義するための境界
- ルート：集約に含まれる特定のオブジェクト

境界内に存在するオブジェクトを外部に曝け出さないことで、集約内の不変条件を維持できるようにしている。
```Scala
val uId:   UserId   = UserId(1)
val uName: UserName = UserName("Bonz")
// 集約
val user:  User     = User(uId, uName)

val name: UserName = UserName("JohnJohn")

// NG: 5文字以上のユーザー名は登録できないなどのルールがあった場合チェックできない（不変条件漏れが起きる）
user.name = name

// OK
val updatedUser = user.changeName(name)
```
境界内に外部から直接アクセスなせないことにより、null チェックなどができる。

## 2. オブジェクトの操作に関する基本的な原則
オブジェクト同士が無秩序にメソッドを呼び出しあうと、不変条件を維持することは難しくなる。

「デメテルの法則」はオブジェクト同士のメソッド呼び出しに秩序をもたらす。メソッドを呼び出すオブジェクトは以下の4つ限定される。
- オブジェクト自身
- 引数として渡されたオブジェクト
- インスタンス変数
- 直接インスタンス化したオブジェクト

例えば、車を運転する時にタイヤに対して直接命令しないように、オブジェクトのフィールドに直接命令するのではなく、それを保持するオブジェクトに命令し、フィールドは保持しているオブジェクト自身が管理する。
```Scala
// 値オブジェクト
case class UserName(v: String) {

  def length: Int = v.length
}

// エンティティ
case class User(name: UserName) {

  def changeName(name: UserName): User =
    name.length > 10 match {
      case false => this.copy(name)
      case true  => throw new IllegalArgumentException("ユーザー名は10文字以下で登録してください")
    }
}

// -------------------------------------------------

val name: UserName = UserName("Kanta715")
val user: User     = User(name)

// 10文字以上でエラーが変える
user.changeName(UserName("NachanPichan"))

// デメテルの原則反した場合
// 不変条件を維持できない check できない
// user.name = UserName("NachanPichan")
```
