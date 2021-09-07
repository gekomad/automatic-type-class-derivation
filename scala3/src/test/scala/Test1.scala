import org.junit.{Assert, Test}

class Test1 {
  @Test def MirroredElemTypes2(): Unit = {
    import core.list2class
    import core.Header.fieldNames

    case class Foo(a: Int, c: String)

    val fields: List[String] = fieldNames[Foo]

    assert(fields == List("a", "c"))

    val list: List[String] = List("1", "foo")

    assert(list2class[Foo](list) == Right(Foo(1, "foo")))

  }
}
