package Knowledge.DomainService

object Main {

  def main(args: Array[String]): Unit = {

    // エンティティに重複ロジックを入れた例
    val user: User = User("Kanta")

    user.exists(user) match {
      case true  => println("NG: 重複しています")
      case false => println("OK: 重複していません")
    }
    // NG: 重複しています
    /**
     * 自分で自分をチェックすることがあり得る
     * そして自分をチェックした時に、真、偽のどちらで返すべきなのか開発者を混乱させる
     */

    // -----------------------------------------------

    UserService.exists(user) match {
      case true  => println("NG: 重複しています")
      case false => println("OK: 重複していません")
    }
    // OK: 重複していません
  }
}
