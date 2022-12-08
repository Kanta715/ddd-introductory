package Application.ApplicationService.ApplicationService.User

import Application.ApplicationService.ValueObject.UserName
import Application.ApplicationService.Entity.User
import Application.ApplicationService.DomainService.UserService
import Application.ApplicationService.Repository.UserRepository

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
