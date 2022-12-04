package Knowledge.DomainService.UseCase.Entity

import Knowledge.DomainService.UseCase.ValueObject.{ UserId, UserName }

case class User(id: UserId, name: UserName)
