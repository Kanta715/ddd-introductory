package Application.ApplicationService.ApplicationService.User

import Application.ApplicationService.ValueObject.UserId
import Application.ApplicationService.Repository.UserRepository
import Application.ApplicationService.DTO.UserData

// UserService がなくなったことで凝集度が高まり、堅牢性・信頼性・再利用性・可読性が上がる
object GetUserApplicationService {

  private val userRepository = UserRepository

  // 取得用メソッド
  def get(id: UserId): Option[UserData] = {
    val userOpt = userRepository.find(id)
    userOpt.map(UserData.build(_))
  }
}
