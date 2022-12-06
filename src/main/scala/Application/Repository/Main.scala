package Application.Repository

object Main {

  def main(args: Array[String]): Unit = {

    if (args.isEmpty) throw new IllegalArgumentException("引数を指定してください")

    val userService = UserService

    val name: String = args.headOption.getOrElse("Pin")

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
  }
}
