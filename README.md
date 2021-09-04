Automatic type class derivation in Scala 2 e Scala 3
======


## Scala 2

```scala

    import core.Schema
    import core.Header._

    case class Foo(a: Int, c: String)

    val fields: List[String] = fieldNames[Foo]

    assert(fields == List("a", "c"))

    val list: List[String]   = List("1", "foo")

    val p: Map[String, String] = fields.zip(list).toMap // Map(a -> 1, c -> foo)
    val schema                 = Schema.of[Foo]
    assert(schema.readFrom(p) == Right(Foo(1, "foo")))

```

## Scala 3

```scala

    import core.Header
    import core.Header._

    case class Foo(a: Int, c: String)

    val fields: List[String] = fieldNames[Foo]

    assert(fields == List("a", "c"))

    ???
```
