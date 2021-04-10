package core

object Header {

  import shapeless._
  import shapeless.ops.record._
  import shapeless.ops.hlist.ToTraversable

  trait FieldNames[_] {
    def apply(): List[String]
  }

  implicit def toNames[T, Repr <: HList, KeysRepr <: HList](
    implicit gen: LabelledGeneric.Aux[T, Repr],
    keys: Keys.Aux[Repr, KeysRepr],
    traversable: ToTraversable.Aux[KeysRepr, List, Symbol]
  ): FieldNames[T] = new FieldNames[T] {
    def apply(): List[String] = keys().toList.map(_.name)
  }

  def fieldNames[T](implicit h: FieldNames[T]): List[String] = h()

}
