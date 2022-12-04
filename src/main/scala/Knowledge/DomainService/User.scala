package Knowledge.DomainService

case class User(name: String) {

  // エンティティに定義
  def exists(user: User): Boolean = {
    name == user.name
  }
}
