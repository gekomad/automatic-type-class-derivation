import org.scalatest.funsuite.AnyFunSuite

class Test extends AnyFunSuite {

  test("Instantiate a class from List[String]") {
    import core.Schema
    import core.Header._

    case class Foo(a: Int, c: String)

    val fields: List[String] = fieldNames[Foo]
    assert(fields == List("a", "c"))

    val list: List[String] = List("1", "foo")

    val schema: Schema[Foo]    = Schema.of[Foo]
    val p: Map[String, String] = fields.zip(list).toMap // Map(a -> 1, c -> foo)
    assert(schema.readFrom(p) == Right(Foo(1, "foo")))

  }

}
