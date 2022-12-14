package AdvancedKnowledge.Aggregation

case class User(name: UserName) {

  def changeName(name: UserName): User =
    name.length > 10 match {
      case false => this.copy(name)
      case true  => throw new IllegalArgumentException("ユーザー名は10文字以下で登録してください")
    }
}
