package Application.Factory

object Main {

  def main(args: Array[String]): Unit = {

    // アプリケーションサービスに生成過程のロジックを出した場合
    val code:     Int            = 3
    val sweetOpt: Option[Sweets] = Sweets.values.find(_.code == code)

    sweetOpt match {
      case Some(sweets) => println(sweets)
      case None         => println("指定されたコードのスイーツは存在しません")
    }

    // ----------------------------------------------------

    // Factory を使った場合
    val code2:  Int    = 1
    val sweets: Sweets = SweetsFactory.create(code2)
    println(sweets)
  }
}
