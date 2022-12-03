## 1. エンティティとは
エンティティは値オブジェクトと対を為すドメインオブジェクトである。

値オブジェクト同様にエンティティもドメインモデルを実装したオブジェクトだが、エンティティは同一性によって識別される。

[同一性とは](https://github.com/Kanta715/ddd-introductory/blob/main/src/main/scala/Knowledge/ValueObject/ValueObject.md#%E5%90%8C%E4%B8%80%E6%80%A7%E3%81%A8%E3%81%AF) 属性によって区別するのではなく、オブジェクトそのものが同じかどうかで識別することをいう。

例えば、人間には年齢、名前、身長、体重などの属性があるが、それらが変わったからといって別人になるわけではない。
その人がその人たる所以は属性と無関係のところにあり、同一性を担保する何かが存在することを示唆している。

ソフトウェアシステムでも同じように、属性で区別されないオブジェクトがあり、それがエンティティである。

## 2. エンティティの性質
エンティティの性質には値オブジェクトと真逆のような性質もある。
性質は以下の3つ。

- 可変である
- 同じ性質であっても区別される
- 同一性により区別される

### 2-1. 可変である
値オブジェクトは不変なオブジェクトだが、エンティティは可変である。

人間が持つ年齢や名前といった属性のように、属性が変化することが許容されている。
```Scala
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
}

// ユーザー名を変更して同じユーザーか確かめる
val bonz: User = User("Bonz")
val taro: User = bonz.changeName("Taro")

// ------------------------------------------------------

// eq で同一性を確かめる
bonz.eq(taro) match {
  case true  => println(s"$bonz と $taro は同一です")
  case false => println(s"同一ではありません")
}
// User(Taro) と User(Taro) は同一です
```

### 2-2. 同じ性質であっても区別される
値オブジェクトは同じ属性であれば同じものとして扱われた。
エンティティはそれと異なり、同じ属性であっても区別される。

例えば、人間の名前を表現したいドメインモデルがあったとする。
このドメインモデルをドメインオブジェクトとして表現する時、値オブジェクトだと表現できない。
もし値オブジェクトの性質をこのドメインモデルに当てはめると、同姓同名の人は同一人物であるということになってしまう。

そんなことはありえないため、エンティティ同士を同一性で判断させる。
```Scala
case class User(var name: String) {
  /**
   * 名前などの属性ではなく eq によって同じインスタンスかどうかで判断
   */
  def equals(that: User): Boolean =
    this eq that
}

// ---------------------------------------------------

val bonz:  User = User("Bonz")
val bonz2: User = User("Bonz")
bonz.equals(bonz2) match {
  case true  => println("同じユーザーです")
  case false => println("違うユーザーです")
}
// 違うユーザーです
```


### 2-3. 同一性により区別される
プログラムでは同一性を判断するために識別子を利用する。

値オブジェクトは属性で等価性を判断するが、エンティティは識別子により同一性を判断する。

```Scala
// 識別子
case class UserId(v: Int)

case class User2(id: UserId, name: String) {

  def changeName(name: String): User2 =
    this.copy(id, name)

  /**
   * 識別子を用いて同一性を判断するメソッド
   */
  def equals(that: User2): Boolean = this.id == that.id
}

// ------------------------------------------------------

val userId:  UserId = UserId(1)
val userId2: UserId = UserId(2)
val shin:    User2  = User2(userId, "Shin")
val kan:     User2  = shin.changeName("Kan")

shin.equals(kan) match {
  case true  => println("同じユーザーです")
  case false => println("違うユーザーです")
}
// 同じユーザーです

val shin2: User2 = User2(userId2, "Shin")
shin.equals(shin2) match {
  case true => println("同じユーザーです")
  case false => println("違うユーザーです")
}
// 同じユーザーです
```

## 3. エンティティの判断基準としてのライフサイクルと連続性
値オブジェクトとエンティティはドメインの概念を実現するオブジェクトとして似通っている。
ライフサイクルが存在し、そこに連続性があるかが判断基準になる。

例えば、あるシステムのユーザーオブジェクトは利用者によって作成される。
そしてシステムを利用し、不要になるとユーザーが削除される。

作成されて生を受け、削除されて死を迎える。このようにユーザーはライフサイクルを持ち、連続性のある概念である。
もしも、ライフサイクルを持たない、またはシステムにとってライフサイクルを表現する意味を持たない場合は、値オブジェクトとして定義する。
