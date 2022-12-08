package Application.ApplicationService.DomainService

import Application.ApplicationService.ValueObject.UserId
import Application.ApplicationService.Entity.User
import Application.ApplicationService.Repository.UserRepository

// ドメインサービス
object UserService {

  private val userRepository = UserRepository

  def checkDuplicatesAtName(user: User): Boolean =
    userRepository.checkDuplicatesAtName(user)
}

