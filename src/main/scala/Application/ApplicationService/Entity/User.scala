package Application.ApplicationService.Entity

import Application.ApplicationService.ValueObject.{ UserId, UserName }

case class User(
  id:   Option[UserId] = None,
  name: UserName
) {
  def changeName(name: String): User =
    this.copy(id, UserName(name))
}
