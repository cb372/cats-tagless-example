package example

import cats._
import cats.implicits._
import cats.tagless._
import cats.tagless.implicits._

object Main {

  implicit val liftIlaError : Either[ILAError, ?] ~> Either[UserFacingError, ?] =
    λ[Either[ILAError, ?] ~> Either[UserFacingError, ?]](_.leftMap {
      case ILAIsDown => InternalServerError
      case UserNotFoundInILA => UserDoesNotExist
    })

  implicit val liftAuthServiceError : Either[AuthServiceError, ?] ~> Either[UserFacingError, ?] =
    λ[Either[AuthServiceError, ?] ~> Either[UserFacingError, ?]](_.leftMap {
      case AuthServiceIsDown => InternalServerError
      case UserNotFoundInAuth => InternalServerError // shouldn't happen if ILA told us the user existed
    })

  val ila: ILA[Either[UserFacingError, ?]] =
    FunctorK[ILA].mapK[Either[ILAError, ?], Either[UserFacingError, ?]](ILAImpl)(liftIlaError)

  val authService: AuthService[Either[UserFacingError, ?]] =
    FunctorK[AuthService].mapK[Either[AuthServiceError, ?], Either[UserFacingError, ?]](AuthServiceImpl)(liftAuthServiceError)

  val program = new Program[Either[UserFacingError, ?]](authService, ila)

  def main(args: Array[String]): Unit =
    println(program.login("chris"))

}
