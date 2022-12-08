package Application.ApplicationService.DTO

import Application.ApplicationService.Entity.User

// DTO
case class UserData(
  id:   Int,
  name: String
)

object UserData {

  def build(user: User): UserData =
    UserData(user.id.map(_.v).getOrElse(0), user.name.v)
}
