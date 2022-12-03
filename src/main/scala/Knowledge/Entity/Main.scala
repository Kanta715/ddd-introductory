package Knowledge.Entity

object Main {

  def main(args: Array[String]): Unit = {

    val bonz: User = User("Bonz")
    val taro: User = bonz.changeName("Taro")

    // eq で同一性を確かめる
    bonz.eq(taro) match {
      case true  => println(s"$bonz と $taro は同一です")
      case false => println(s"同一ではありません")
    }
    // User(Taro) と User(Taro) は同一です

    // ------------------------------------------------------

    val bonz2: User = User("Bonz")
    bonz.equals(bonz2) match {
      case true  => println("同じユーザーです")
      case false => println("違うユーザーです")
    }
    // 違うユーザーです

    // ------------------------------------------------------

    val userId:  UserId = UserId(1)
    val userId2: UserId = UserId(2)
    val shin:    User2  = User2(userId, "Shin")
    val kan:     User2  = shin.changeName("Kan")

    shin.equals(kan) match {
      case true  => println("同じユーザーです")
      case false => println("違うユーザーです")
    }
    // 同じユーザーです

    val shin2: User2 = User2(userId2, "Shin")
    shin.equals(shin2) match {
      case true => println("同じユーザーです")
      case false => println("違うユーザーです")
    }
    // 違うユーザーです
  }
}
