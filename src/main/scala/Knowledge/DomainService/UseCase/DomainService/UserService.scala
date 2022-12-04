package Knowledge.DomainService.UseCase.DomainService

import Knowledge.DomainService.UseCase.ValueObject.{ UserId, UserName }

class UserService {

  def create(name: String): Unit = {

    val userName: UserName = UserName(name)

    val count:  Int = ??? // sql.execute("SELECT COUNT(id) FROM user;"): 適当なSQLです
    val nextId: Int = count + 1

    val userId: UserId = UserId(nextId)

    val exists: Boolean = ??? // sql.execute("SELECT EXISTS (SELECT * FROM user WHERE id = $nextId")

    if (!exists) {
      // seq.execute("INSERT INTO user (id, name) VALUES ($nextId, $userName)")
      println("作成しました")
    } else {
      println("作成に失敗しました")
    }
  }
}
