package core

object Header{
  import scala.compiletime.{constValue, erasedValue}
  import scala.compiletime.ops.int.S
  import scala.deriving._

  private inline def toNames[T <: Tuple]: List[String] =
    inline erasedValue[T] match {
    case _: (head *: tail) => (inline constValue[head] match {
      case str: String => str
    }) :: toNames[tail]
    case _ => Nil
  }

  inline def fieldNames[P <: scala.Product](using mirror: Mirror.Of[P]): List[String] = toNames[mirror.MirroredElemLabels]

}
