## 1. ファクトリとは
ドメインオブジェクトの生成処理をオブジェクトとして定義し、生成を責務とするオブジェクトのことを「ファクトリ」という。

## 2. ファクトリの目的
ドメインには複雑な内部構造をもつものが多くある。複雑な内部構造を持つドメインは生成過程も複雑になっていることが多く、その生成過程はドメインを理解する知識となる。

この生成処理をドメインオブジェクトに記述すると、ドメインモデルを表現するドメインオブジェクトの趣旨をぼやけさせる。かといって、アプリケーションサービスに生成処理を任せるのは良い方針ではない。

そこで、オブジェクトの生成を責務とするファクトリを定義することで、ドメインオブジェクトの生成に関する知識をまとめられる。

## 3. ファクトリを使って実装する
今回はあらゆるスイーツの中から code により何らかのスイーツを生成する処理を定義する。
```Scala
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

// -------------------------------------------------------------------------

object SweetsFactory {

  def create(sweetsCode: Int): Sweets = {
    val sweetsOpt: Option[Sweets] = Sweets.values.find(_.code == sweetsCode)
    sweetsOpt match {
      case Some(sweets) => sweets
      case None         => throw new IllegalArgumentException("指定したコードのスイーツは存在しません")
    }
  }
}
```

まずは良くない例から。
```Scala
// アプリケーションサービスに生成過程のロジックを出した場合
val code:     Int            = 3
val sweetOpt: Option[Sweets] = Sweets.values.find(_.code == code)

sweetOpt match {
  case Some(sweets) => println(sweets)
  case None         => println("指定されたコードのスイーツは存在しません")
}
```

次に Factory を定義する。
```Scala
// Factory を使った場合
val code2:  Int    = 1
val sweets: Sweets = SweetsFactory.create(code2)
println(sweets)
```
生成処理をファクトリにカプセル化することで全く同じ処理をアプリケーションサービスに記述することを防げる。そして、ドメインオブジェクトに複雑な生成処理が含まれることを防ぐことができ、ドメインモデルを表現したいドメインオブジェクトに余計な記述をなくすことができる。