package Knowledge.DomainService

object UserService {

  private val EXISTS_USERS: Seq[User] = Seq(
    User("Pin"),
    User("Pon"),
    User("Pan")
  )

  def exists(user: User): Boolean =
    EXISTS_USERS.exists(_.name == user.name)
}
