package Knowledge.Entity

case class User(var name: String) {

  /**
   * name を変えるメソッド
   *
   * 値オブジェクトでは値自体を変えることになるので NG だが
   * エンティティは可変を許容するためメンバ変数を変えることは OK
   */
  def changeName(name: String): User = {
    this.name = name
    this
  }

  /**
   * 名前などの属性ではなく eq によって同じインスタンスかどうかで判断
   */
  def equals(that: User): Boolean =
    this eq that

}

// 識別子
case class UserId(v: Int)

case class User2(id: UserId, name: String) {

  def changeName(name: String): User2 =
    this.copy(id, name)

  /**
   * 識別子を用いて同一性を判断するメソッド
   * （Scalaには必要ないが一応）
   */
  def equals(that: User2): Boolean = this.id == that.id
}
