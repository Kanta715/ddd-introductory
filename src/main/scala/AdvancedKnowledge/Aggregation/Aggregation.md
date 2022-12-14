## 集約とは
オブジェクト指向プログラミングでは複数のオブジェクトがまとめられ、ひとつの意味を持ったオブジェクトが構築される。こうしたオブジェクトのグループには維持されるべき不変条件がある。

集約は不変条件を維持する単位として切り出され、オブジェクトの操作に秩序をもたらす。

集約には境界とルートが存在する
境界：集約に何が含まれるかを定義するための境界
ルート：集約に含まれる特定のオブジェクト

境界内に存在するオブジェクトを外部に曝け出さないことで、集約内の不変条件を維持できるようにしている。
```Scala
val uId:   UserId   = UserId(1)
val uName: UserName = UserName("Bonz")
val user:  User     = User(uId, uName)

val name: UserName = UserName("Jon")

// NG
user.name = name

// Ok
val updatedUser = user.changeName(name)
```

