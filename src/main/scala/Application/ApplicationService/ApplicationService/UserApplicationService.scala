package Application.ApplicationService.ApplicationService

import Application.ApplicationService.ValueObject.{UserId, UserName}
import Application.ApplicationService.Entity.User
import Application.ApplicationService.DomainService.UserService
import Application.ApplicationService.DTO.UserData

// アプリケーションサービス
object UserApplicationService {

  private val userService = UserService

  // 登録用メソッド
  def register(name: String): Unit = {
    val userName: UserName = UserName(name)
    val user:     User     = User(None, userName)

    val exists: Boolean = userService.checkDuplicatesAtName(user)
    exists match {
      case true =>
        println(
          """
            |ユーザー名が重複しています
            |ユーザー名を変更して再度登録してください
            |""".stripMargin)
      case false =>
        userService.create(user)
        println("ユーザーを作成しました")
    }
  }
  // アプリケーションサービスにドメインルールが流出しないようにする
  // ドメインルールの変更時に改修の幅が大きくなる
  // def register(name: String): Unit = {
  //    val userName: UserName = UserName(name)
  //
  //    val userOpt: Option[User] = userService.findAtName(userName)
  //    userOpt.match match { <- ドメインのルールが変わると全ての記述を改修する必要がある
  //      case true =>
  //        println(
  //          """
  //            |ユーザー名が重複しています
  //            |ユーザー名を変更して再度登録してください
  //            |""".stripMargin)
  //      case false =>
  //        userService.create(userName)
  //        println("ユーザーを作成しました")
  //    }
  //  }


  // 取得用メソッド
  /**
   * User を返すと changeName メソッドが生えているためアプリケーションサービス以外でドメインオブジェクトの操作が可能になる
   * それを防ぐために、必要なデータだけを持った UserData を返すようにしている
   */
  def get(id: UserId): Option[UserData] = {
    val userOpt = userService.find(id)
    userOpt.map(UserData.build(_))
  }

  // 更新用メソッド
  // 既存のものがない場合は登録
  def update(user: User): User =
    userService.updateOrCreate(user)

  // 削除用メソッド
  def delete(id: UserId): Unit = {
    val userOpt = userService.delete(id)
    userOpt.isDefined match {
      case true  => println("ユーザーを削除しました")
      case false => println("ユーザーが見つかりませんでした")
    }
  }
}
