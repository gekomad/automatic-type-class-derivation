//package core
//
//import labelled._
//
//sealed trait Schema[A] {
//  def readFrom(input: Map[String, String]): Either[String, A]
//}
//
//object Schema {
//  def of[A](implicit s: Schema[A]): Schema[A] = s
//
//  private def instance[A](body: Map[String, String] => Either[String, A]): Schema[A] = new Schema[A] {
//    def readFrom(input: Map[String, String]): Either[String, A] =
//      body(input)
//  }
//
//  implicit val noOp: Schema[HNil] = new Schema[HNil] {
//    def readFrom(input: Map[String, String]): Either[Nothing, HNil.type] = Right(HNil)
//  }
//
//  implicit def parsing[K <: Symbol, V: Convert, T <: HList](
//    implicit key: Witness.Aux[K],
//    next: Schema[T]
//  ): Schema[FieldType[K, V] :: T] = Schema.instance { input =>
//    val a: Either[String, FieldType[K, V]] = input
//      .get(key.value.name)
//      .fold(Left(s"${key.value.name} is missing"): Either[String, V])(entry => Convert.to[V](entry))
//      .map(field[K](_))
//
//    for {
//      a11 <- a
//      a22 <- next.readFrom(input)
//    } yield a11 :: a22
//
//  }
//
//  implicit def instantiateClass[A, R <: HList](
//    implicit repr: LabelledGeneric.Aux[A, R],
//    schema: Schema[R]
//  ): Schema[A] = {
//    val o = Schema.instance { theMap =>
//      val x: Either[String, R] = schema.readFrom(theMap)
//      val z: Either[String, A] = x.map(x => repr.from(x))
//      z
//    }
//    o
//  }
//}
