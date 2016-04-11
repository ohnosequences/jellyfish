
```scala
package ohnosequences.jellyfish.api

case class uint64(private val input: BigInt) {

  // NOTE: it is important not to pass to jellyfish an invalid value,
  //  so we take abs of the passes BigInt and compare it to the MaxValue
  def value: BigInt =
    uint64.MaxValue.input.min(input.abs)
    // NOTE: .abs is arguable, probably this is a more intuitive convention:
    // uint64.MaxValue.input.min(
    //   uint64.MinValue.input.max(
    //     input
    //   )
    // )

  override def toString = this.value.toString

  // just an alias:
  def apply(str: String): uint64 = uint64(BigInt(str))
}

case object uint64 {

  val MinValue: uint64 = uint64(0)
  val MaxValue: uint64 = uint64(BigInt(Long.MaxValue) * 2 + 1)
}

```




[test/scala/Jellyfish.scala]: ../../../test/scala/Jellyfish.scala.md
[main/scala/api/options.scala]: options.scala.md
[main/scala/api/package.scala]: package.scala.md
[main/scala/api/expressions.scala]: expressions.scala.md
[main/scala/api/uint64.scala]: uint64.scala.md
[main/scala/api/commands/histo.scala]: commands/histo.scala.md
[main/scala/api/commands/queryAll.scala]: commands/queryAll.scala.md
[main/scala/api/commands/query.scala]: commands/query.scala.md
[main/scala/api/commands/dump.scala]: commands/dump.scala.md
[main/scala/api/commands/merge.scala]: commands/merge.scala.md
[main/scala/api/commands/bc.scala]: commands/bc.scala.md
[main/scala/api/commands/count.scala]: commands/count.scala.md