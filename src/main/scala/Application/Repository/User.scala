package Application.Repository

case class User(
  id:   Option[UserId] = None,
  name: UserName
)
