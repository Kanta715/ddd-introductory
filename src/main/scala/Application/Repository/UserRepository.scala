package Application.Repository

object UserRepository {

  private val EXISTS_USERS: Seq[User] = Seq(
    User(Some(UserId(1)), UserName("Pin")),
    User(Some(UserId(2)), UserName("Pon")),
    User(Some(UserId(3)), UserName("Pan"))
  )

  // 名前での重複チェック
  def checkDuplicatesAtName(user: User): Boolean =
    // sql.execute("SELECT EXISTS (SELECT * FROM user WHERE id = $user.id")
    EXISTS_USERS.exists(_.name == user.name)

  // ユーザー作成
  def create(user: User): User =
    // seq.execute("INSERT INTO user (name) VALUES ($user.name)")
    user.copy(Some(UserId(4)), user.name)

  // ユーザー取得
  def find(id: UserId): Option[User] =
    // sql.execute("SELECT * FROM user WHERE id = $id;")
    EXISTS_USERS.find(_.id.exists(_ == id))
}
