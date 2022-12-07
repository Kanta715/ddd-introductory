package Application.ApplicationService

import Application.ApplicationService.ApplicationService.UserApplicationService
import Application.ApplicationService.Entity.User
import Application.ApplicationService.Repository.UserRepository
import Application.ApplicationService.ValueObject.{ UserId, UserName }

object Main {

  def main(args: Array[String]): Unit = {
    val userAppService = UserApplicationService

    println("作成処理")
    val name: String = "Kan715"
    userAppService.register(name)

    // -------------------------
    println
    println("取得処理")
    val id: UserId = UserId(2)
    userAppService.get(id) match {
      case Some(user) =>
        println(
          s"""
            |ユーザーを取得しました
            |$user
            |""".stripMargin)
      case None  =>
        println("ユーザーが見つかりませんでした")
    }

    // --------------------------
    println
    println("更新処理")
    val updatedUser = User(Some(UserId(2)), UserName("PON"))
    val user = userAppService.update(updatedUser)
    println(
      s"""
        |ユーザーを更新しました
        |$user
        |""".stripMargin)

    // --------------------------
    println
    println("削除処理")
    val id2: UserId = UserId(1)
    userAppService.delete(id2)
  }
}
