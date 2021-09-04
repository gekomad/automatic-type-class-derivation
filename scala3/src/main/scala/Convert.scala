package core

trait Convert[V]:
  def parse(input: String): Either[String, V]

object Convert:

  def to[V](input: String)(implicit C: Convert[V]): Either[String, V] = C.parse(input)

  def apply[V](body: String => Either[String, V]): Convert[V] = (input: String) => body(input)

  type Cons[A] = String => A

  implicit def gen[A](implicit conv: Convert[A]): Convert[A] = Convert(conv.parse)

  implicit val _toInt: Convert[Int] = (a: String) =>
    a.toIntOption match {
      case Some(value) => Right(value)
      case None        => Left(s"$a is not Int")
  }

  implicit val _toString: Convert[String] = Convert(a => Right(a))

  //add other converters

  //implicit val _to...
  //implicit val _to...


end Convert
