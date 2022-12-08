package Application.ApplicationService.Repository

import Application.ApplicationService.ValueObject.{ UserId, UserName }
import Application.ApplicationService.Entity.User

// リポジトリ
object UserRepository {

  private var EXISTS_USERS: Seq[User] = Seq(
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

  // 更新
  def updateOrCreate(user: User): User = {
    val userOpt = EXISTS_USERS.find(_.id == user.id)
    userOpt match {
      case Some(value) =>
        // seq.execute("UPDATE ...")
        EXISTS_USERS = EXISTS_USERS.filterNot(_.id == value.id) :+ user
        user
      case None =>
        // seq.execute("INSERT INTO user (name) VALUES ($user.name)")
        EXISTS_USERS = EXISTS_USERS :+ user
        user
    }
  }

  // 削除
  def delete(id: UserId): Option[User] = {
    val userOpt = EXISTS_USERS.find(_.id.exists(_ == id))
    userOpt match {
      case Some(user) =>
        EXISTS_USERS = EXISTS_USERS.filterNot(_ == user)
        userOpt
      case _ => None
    }
  }
}
