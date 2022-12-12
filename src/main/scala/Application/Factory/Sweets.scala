package Application.Factory

sealed abstract class Sweets(val code: Int, val name: String)

object Sweets {

  case object Pancake   extends Sweets(code = 1, name = "Pancake")
  case object IceCream  extends Sweets(code = 2, name = "IceCream")
  case object Chocolate extends Sweets(code = 3, name = "Chocolate")

  // 全ての列挙型を取得
  val values: Seq[Sweets] = Seq(
    Pancake,
    IceCream,
    Chocolate
  )
}
