## 1. 依存とは
依存はあるオブジェクトからあるオブジェクトを参照だけで発生する。

オブジェクト B は、オブジェクト A に依存している。
```Scala
object A

object B {
  val a = A
}
```

例えば、[GetUserApplicationService](https://github.com/Kanta715/ddd-introductory/blob/main/src/main/scala/Application/ApplicationService/ApplicationService/User/GetUserApplicationService.scala) では、`userRepository` に依存している。
```Scala
object GetUserApplicationService {

  private val userRepository = UserRepository

  def get(id: UserId): Option[UserData] = {
    val userOpt = userRepository.find(id)
    userOpt.map(UserData.build(_))
  }
}
```
`userRepository` で扱っているデータストアが RDB なのか、NoSQL であたうのかということは `GetUserApplicationService` から知らないが、いずれかのデータストアに結びついていることは確かである。

ソフトウェアでが健全に成長するためには、開発やテストで気軽にコードを実行できるように仕向ける必要がある。特定のデータストアに結びついてしまうと、それは不可能になる。

ここでは、抽象クラスを実装してアプリケーションサービスのコンストラクトでリポジトリをインスタンス化することにより、依存の向き先を抽象型にする。そうすることで、本番用、テスト用でリポジトリを分けることができ、特定のデータストへの結びつきもなくなる。
```Scala
abstract class AbstractUserRepository {
  // ...
}

object UserRepository extends AbstractUserRepository {
  // ...
}

object UserTestRepository extends AbstractUserRepository {
  // ...
}

case class GetUserApplicationService(userRepository: AbstractUserRepository) {
  // ...
}

// ------------------------

// Test
val userAppTestService = GetUserApplicationService(UserTestRepository)

// Production
val userAppService = GetUserApplicationService(UserRepository)
```

スタートアップスクリプト？を利用する方法もあるが、設定ファイルの記述など面倒くさいので省く

## 2. 依存関係逆転の原則
### 2-1. 上位レベルのモジュールは下位レベルのモジュールに依存してはならない。どちらのモジュールも抽象に依存するべきである。
プログラムには、レベルと呼ばれる概念がある。レベルは入出力からの距離を示す。低レベルは機械に近い具体的な処理を示し、高レベルは人間に近い抽象的な処理を示す。依存関係逆転の原則の上位レベルや下位レベルというはこれと同じ。
ここでいう `UserRepository` は、`UserRepository` を操作する `GetUserApplicationService`よりも下位のモジュールになる。

1 での実装のように　`AbstractUserRepository` を実装して抽象化することに上モジュールが下位モジュールに依存せず抽象に依存することができ、柔軟性があがる。

### 2-2. 抽象は、実装の詳細に依存してはならない。実装の詳細が抽象に依存するべきである。
抽象が詳細に依存するようになると、低レベルのモジュールの方針変更が高レベルのモジュールに波及する。
重要なドメインのルールが含まれるのは高レベルのモジュールのため、低レベルのモジュールの変更による、高レベルのモジュールの変更はなるべくしたくない。

低レベルなモジュールは、高レベルなモジュールに合わせて実装を行うことで、より重要な高次元の概念に主導権を握らせることができる。
