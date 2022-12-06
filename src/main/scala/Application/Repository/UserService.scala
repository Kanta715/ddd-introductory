package Application.Repository

object UserService {

  private val userRepository = UserRepository

  def checkDuplicatesAtName(user: User): Boolean =
    userRepository.checkDuplicatesAtName(user)

  def create(user: User): User =
    userRepository.create(user)

  def find(id: UserId): Option[User] =
    userRepository.find(id)
}
