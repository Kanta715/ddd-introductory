package Knowledge.ValueObject

case class FullName(
  lastName:  String, // 苗字
  firstName: String  // 名前
) {

  lazy val fullNameAtJapaneseNotation: String = lastName + firstName
  lazy val fullNameAtAmericanNotation: String = firstName + lastName
}
