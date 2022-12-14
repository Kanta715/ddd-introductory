package Application.Factory

object SweetsFactory {

  def create(sweetsCode: Int): Sweets = {
    val sweetsOpt: Option[Sweets] = Sweets.values.find(_.code == sweetsCode)
    sweetsOpt match {
      case Some(sweets) => sweets
      case None         => throw new IllegalArgumentException("指定したコードのスイーツは存在しません")
    }
  }
}
