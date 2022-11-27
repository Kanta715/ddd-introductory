package Knowledge.ValueObject

object Main {

  def main(args: Array[String]): Unit = {

    // Scala のプリミティブな値だけでフルネームを表現したり、処理する場合
    val fullNameOfJapanese = "大谷 翔平"
    val fullNameOfAmerican = "John Smith"

    //  苗字を取得したい
    val lastNameOfJapanese = fullNameOfJapanese.split(" ")(0)
    val lastNameOfAmerican = fullNameOfAmerican.split(" ")(0)

    /**
     * 苗字を出力したいが、世界には名前が前にくる場合もある
     * うまく苗字を出力できていない
     */
    println("プリミティブな値だけで処理")
    println(lastNameOfJapanese) // 大谷: OK
    println(lastNameOfAmerican) // John: NG
    println

    // --------------------------------------------------------------

    // 値オブジェクトを使って表現、処理する場合
    val valueObjectFullNameJPN: FullName = FullName("大谷", "翔平")
    val valueObjectFullNameUSA: FullName = FullName("Smith", "John")

    println("値オブジェクトを使って処理")
    println(valueObjectFullNameJPN.lastName)
    println(valueObjectFullNameUSA.lastName)
    println

    println("比較処理")
    println("==: 等価性を判断")
    val valueObjectFullNameJPN2: FullName = FullName("大谷", "翔平")
    println(valueObjectFullNameJPN == valueObjectFullNameJPN2) // true
    println("equals: 等価性を判断")
    println(valueObjectFullNameJPN equals valueObjectFullNameJPN2) // true
    println("eq: 同一性を判断")
    println(valueObjectFullNameJPN eq valueObjectFullNameJPN2) // false
  }
}
