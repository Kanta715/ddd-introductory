## 1. 値オブジェクトとは
プログラミング言語のプリミティブな値をシステム固有の値として表現するためのパターン

## 2. 値の性質
値の代表的な3つの性質
1. 不変である
2. 交換が可能である
3. 等価性によって比較される

値オブジェクトはシステム固有の値の表現であり、値の一種である

値が持つ性質は値オブジェクトにそのまま適用される

### 2-1. 不変である
```Scala
var greet = "こんにちは"
println(greet) // こんにちは

greet = "Hello"
println(greet) // Hello
```
greet の値は最初は `こんにちは` になっており、出力した後に `Hello` が再代入されている。したがって値は変更できるものであると考えられる。

しかし、厳密には値自体の変更はしていない。ここでは「代入」を利用しているため、値そのものが変わっているのではなく、変数の中の値が変わっている。

値は一貫して変化することがない。値が変化できてしまった場合は以下のようなコードになる。
```Scala
var greet = "こんにちは"
greet.changeTo("Hello") // 存在しないメソッド
println(greet) // Hello

// ↑ のコードが許されるのであればこのコードも許されることになる
"こんにちは".changeTo("Hello")
println("こんにちは") // Hello
```
changeTo メソッドを値を変更する振る舞いである。値自体の変更は安心して値を利用できなくなる。

```Scala
var fullName = FullName("大谷", "翔平")
fullName.changeLastName("三浦")
```
こういったコードはサンプルコードなどでよく見かけるが、FullName クラスを値ととらえた場合このコードは値を変更しているため不自然である。
changeLastName メソッドは「不変である」という値の性質上、クラスに定義するべきメソッドではない。

#### 不変のメリット/デメリット
**メリット**
- 生成したインスタンスの状態が自分の知らないところで変更されている事態を防ぐことができる

**デメリット**
- オブジェクトの値を一部でも変更する場合、新たなインスタンスが生成されること

### 2-2. 交換が可能である
値は不変だが、開発するにあたって値を変更しないことはない。
値オブジェクトもプリミティブな値を変更する時と、同じような変更方法でなければならない。（代入操作によって変更）
```Scala
import Knowledge.ValueObject.FullName
var num = 0
num = 1
println(num) // 1

// プリミティブな値と変更方法は同じ
var fullName = FullName("大谷", "翔平")
fullName = FullName("山本", "由伸")

// Scala はデフォルトで immutable(不変)な変数のため再代入をすることも少ない
val fullName2 = FullName("根尾", "昂")
fullName2 = FullName("藤原", "恭大") // 代入できない
```

### 2-3. 等価性によって比較される
#### 等価性とは
同値性とも呼ばれることもあるそう。（名前的にこっちの方が直感的）

ある2つのオブジェクトが全く同じ値を持つ時、等価という。

Scala では、==, equals で等価性を判断できる。

### 同一性とは
似た言葉に同一性がある。

ある2つのオブジェクトが同一のもの（同じインスタンス）であれば同一と見なされる。

Scala では eq で同一性を判断できる。
```Scala
val str  = "A"
val str2 = "A"

str == str2 // true

val fullName  = FullName("大谷", "翔平")
val fullName2 = FullName("大谷", "翔平")

// 等価性を判断
fullName == fullName2 // true
fullName equals fullName2 // true

// 同一性を判断
fullName eq fullName2 // false
```
値オブジェクトはプリミティブな値と同じように等価性を判断できるメソッドを持たなければならない。
Scala の場合は、標準で備わっている。FullName メソッドに等価を判断するメソッドがない場合、等価を判断するための式が散らばるため、標準でない場合はクラスに定義すること。

## 3. 値オブジェクトを採用するモチベーション

値オブジェクトを採用するモチベーションは主に次の4つ
- 表現力を増す
- 不正な値を存在させない
- 誤った代入を防ぐ
- ロジックの散財を防ぐ

### 3-1. 表現力を増す
複数の要素からなる識別子などは、プリミティブな値だけで表現すると直感的にわかりにくいものになる。
```Scala
/**
 * プリミティブな値だけで工業製品などの製品番号などを表す場合
 * 何らかの3つの値で構成されている番号ということはわかる
 */
val modelNumber = "a2088-100-1"

/**
 * 受け取る側は文字列ということしかわからない
 */
def xxxMethod(modelNumber: String): Unit = ???

// -------------------------------------------

/**
 * 値オブジェクトで表した場合
 * 製品番号はプロダクトコード、枝番、ロット番号から構成されていることがわかる
 */
case class ModelNumber(
  productCode: String,
  branch:      String,
  lot:         String
) {
  lazy val code = productCode + branch + lot
}

/**
 * 受け取る側も ModelNumber を見れば直感的にわかる
 */
def xxxMethod(modelNumber: ModelNumber): Unit = ???
```
値オブジェクトで表現することで、直感的でわかりやすい表現をすることができる。

### 3-2. 不正な値を存在させない
システムの値にはルールが存在する。
例えば、ID を数字で表現し、正の数のみというルールをつけた場合
```Scala
// --------------------------------------

/**
 * プリミティブな値の Int だと負の数も表現できてしまうため制限する
 * ファクトリメソッドで生成時に制限する
 */
case class ID(id: Int)
object ID {
  def build(id: Int): ID =
    if (id >= 0) ID(id) else throw new IllegalArgumentException
}
```

### 3-3. 誤った代入を防ぐ
プリミティブな値だけで表現していると、誤った代入に気付きにくい。
```Scala
// プリミティブな値だけで表現
case class User(id: String, name: String)
object User {
  def build(id: String, name: String): User = {
    // コンパイルが通り、実行されてからミスに気付く
    User(name, id)
  }
}

// --------------------------------------------

case class ID(id: String)
case class User(id: ID, name: String)
object User {
  def build(id: String, name: String): User = {
    // コンパイルで引っかかる
    User(name, ID(id))
  }
}
```

### 3-4. ロジックの散財を防ぐ
3-2 でもあったようにシステムにはルールが存在する。
同じルールを複数回書くということはルールの変更があった場合、複数回ルールの変更が必要になる。
値オブジェクトにすることで、ファクトリメソッドにそのロジックを集中させ変更箇所を1箇所にすることができる。
```Scala
// プリミティブな値の場合
val name = ""

// このロジックが散財する
val userName: String = if (name.isEmpty) throw new IllegalArgumentException else name
def createUser(name: String): Unit = ???

createUser(userName)

// ---------------------------------------

// 値オブジェクトの場合
case class Name(lastName: String, firstName: String)
// ロジックを共通化できる
object Name {
  def build(latName: String, firstName: String): Name =
    (latName.isEmpty || firstName.isEmpty) match {
      case false => Name(latName, firstName)
      case true  => throw new IllegalArgumentException
    }
}

val name = Name.build("", "")

def createUser(name: Name): Unit = ???

createUser(name)
```
