package example

import cats.tagless._

@autoFunctorK(autoDerivation = false)
trait ILA[F[_]] {

  def lookupUser(username: String): F[Unit]

}

sealed trait ILAError
case object ILAIsDown extends ILAError
case object UserNotFoundInILA extends ILAError

object ILAImpl extends ILA[Either[ILAError, ?]] {

  def lookupUser(username: String): Either[ILAError, Unit] =
    Left(UserNotFoundInILA)

}
