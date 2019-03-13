package example

import cats.tagless._
import AuthService._

@autoFunctorK(autoDerivation = false)
trait AuthService[F[_]] {

  def login(username: String): F[Session]

}

object AuthService {

  case class Session(id: String)

}

sealed trait AuthServiceError
case object AuthServiceIsDown extends AuthServiceError
case object UserNotFoundInAuth extends AuthServiceError

object AuthServiceImpl extends AuthService[Either[AuthServiceError, ?]] {

  def login(username: String): Either[AuthServiceError, Session] =
    Left(AuthServiceIsDown)

}
