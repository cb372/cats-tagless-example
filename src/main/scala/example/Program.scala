package example

import cats._
import cats.syntax.all._

class Program[F[_]: Monad](authService: AuthService[F], ila: ILA[F]) {

  def login(username: String): F[LoginSuccess] = {
    for {
      ilaResult <- ila.lookupUser(username)
      authServiceSession <- authService.login(username)
    } yield LoginSuccess(authServiceSession.id)
  }

}

case class LoginSuccess(authServiceSessionId: String)

sealed trait UserFacingError

case object UserDoesNotExist extends UserFacingError
case object InternalServerError extends UserFacingError

