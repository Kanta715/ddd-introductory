package Application.ApplicationService.DomainService

import Application.ApplicationService.ValueObject.UserId
import Application.ApplicationService.Entity.User
import Application.ApplicationService.Repository.UserRepository

// ドメインサービス
object UserService {

  private val userRepository = UserRepository

  def checkDuplicatesAtName(user: User): Boolean =
    userRepository.checkDuplicatesAtName(user)

  def create(user: User): User =
    userRepository.create(user)

  def find(id: UserId): Option[User] =
    userRepository.find(id)

  def updateOrCreate(user: User): User =
    userRepository.updateOrCreate(user)

  def delete(id: UserId): Option[User] =
    userRepository.delete(id)
}

