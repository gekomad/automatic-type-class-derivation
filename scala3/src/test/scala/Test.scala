import org.scalatest.funsuite.AnyFunSuite

object JsonEncoderInstances {

  import scala.compiletime.{constValue, erasedValue}
  import scala.compiletime.ops.int.S
  import scala.deriving._
  import scala.compiletime.summonInline
  
  trait JsonEncoder[T]:
    def asJson(encoded: T): String
  
  implicit val a1: JsonEncoder[String] =new JsonEncoder[String]{
    def asJson(encoded: String): String = s"""$encoded"""
  }

  implicit val a2: JsonEncoder[Double] =new JsonEncoder[Double]{
    def asJson(encoded: Double): String = s"""$encoded"""
  }

//  inline def encode[T](using elemEncoder: JsonEncoder[T]) : JsonEncoder[List[T]] = new  JsonEncoder[List[T]]{
//    def asJson(v: Seq[T]): String = "[" + v.map(elemEncoder.asJson).mkString(", ") + "]"
//  }

  inline def summonTypeClassInstances[T <: Tuple]: List[JsonEncoder[_]] = inline erasedValue[T] match {
    case _: EmptyTuple => Nil
    case _: (t *: ts) => summonInline[JsonEncoder[t]] :: summonTypeClassInstances[ts]
  }
  inline def derived[T](using  m: Mirror.Of[T]): JsonEncoder[T] = {
//  inline given derived[T](using m: Mirror.Of[T]) as JsonEncoder[T] = {
    val encoders = summonTypeClassInstances[m.MirroredElemTypes]
    inline m match {
      case s: Mirror.SumOf[T] => jsonSum(s, encoders)
      case p: Mirror.ProductOf[T] => jsonProd(p, encoders)
    }
  }
  def jsonSum[T](s: Mirror.SumOf[T], encoders: List[JsonEncoder[_]]): JsonEncoder[T] =
    new JsonEncoder[T] {
      def asJson(encoded: T): String = {
        val index = s.ordinal(encoded)
        ???
//        encode(encoders(index))(encoded)
      }
    }

  private inline def summonProductElemNames[T <: Tuple]: List[String] = inline erasedValue[T] match {
    case _: EmptyTuple => Nil
    case _: (t *: ts) => constValue[t].toString :: summonProductElemNames[ts]
  }

  inline def jsonProd[T](s: Mirror.ProductOf[T], encoders: List[JsonEncoder[_]]): JsonEncoder[T] = {
    val elemNames = summonProductElemNames[s.MirroredElemLabels]
    new JsonEncoder[T] {
      def asJson(encoded: T): String = {
        if (encoders.nonEmpty) {
          ???
//          "{" + iterator(encoded)
//            .zip(elemNames)
//            .zip(encoders.iterator)
//            .map { case ((enc, name), encoder) => "\"" + name + "\":" + encode(encoder)(enc) }.mkString(",") + "}"
        } else {
          summon[JsonEncoder[String]].asJson(encoded.toString)
        }
      }
    }
  }
}


class Test extends AnyFunSuite:

  test("Instantiate a class from List[String]") {
    import core.Header
    import core.Header._

    case class Foo(a: Int, c: String)
    val fields: List[String] = fieldNames[Foo]

    assert(fields == List("a", "c"))

    ???

  }

end Test
