package AdvancedKnowledge.Aggregation

object Main {

  def main(args: Array[String]): Unit = {

    val name: UserName = UserName("Kanta715")
    val user: User     = User(name)

    // 10文字以上でエラーが変える
    user.changeName(UserName("NachanPichan"))

    // デメテルの原則反した場合
    // 不変条件を維持できない check できない
    // user.name = UserName("NachanPichan")

  }
}
