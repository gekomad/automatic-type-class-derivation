import org.scalatest.funsuite.AnyFunSuite

class Test extends AnyFunSuite {

  test("Instantiate a class from List[String]") {
    import core.Schema
    import core.Header._

    case class Foo(a: Int, c: String)

    val fields: List[String] = fieldNames[Foo]
    val list: List[String]   = List("1", "foo")

    val p: Map[String, String] = fields.zip(list).toMap // Map(a -> 1, c -> foo)
    val schema                 = Schema.of[Foo]
    assert(schema.readFrom(p) == Right(Foo(1, "foo")))

  }

}
