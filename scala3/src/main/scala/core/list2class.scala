package core

// code adapted from
// https://stackoverflow.com/questions/69076456/scala-3-instantiate-class-from-liststring

object list2class:
  import scala.deriving.Mirror

  trait Decoder[A, B] extends (A => Either[String, B])
  object Decoder:
    given Decoder[String, String] = Right(_)
    given Decoder[String, Int] = a =>
      a.toIntOption.toRight(s"$a value is not valid Int")
    given Decoder[String, Double] = a =>
      a.toDoubleOption.toRight(s"$a value is not valid Double")
    given Decoder[List[String], EmptyTuple] = {
      case Nil => Right(EmptyTuple)
      case s   => Left(s"$s empty list")
    }

    given [H, T <: Tuple](using
                          dh: Decoder[String, H],
                          dt: Decoder[List[String], T]
                         ): Decoder[List[String], H *: T] = {
      case h :: t =>
        (dh(h), dt(t)) match {
          case (Right(a), Right(b)) => Right(a *: b)
          case (Left(e), Left(e2))  => Left(s"${e.toString} ${e2.toString}")
          case (Left(e), _)         => Left(e.toString)
          case (_, Left(e))         => Left(e.toString)
        }
      case Nil => Left("empty list")
    }

  def apply[A](xs: List[String])(using
                                 m: Mirror.ProductOf[A],
                                 d: Decoder[List[String], m.MirroredElemTypes]
  ) =
    d(xs) match {
      case Right(r) => Right(m.fromProduct(r))
      case l        => l
    }
end list2class
