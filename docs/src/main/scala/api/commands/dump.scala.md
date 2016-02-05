
```scala
package ohnosequences.jellyfish.api.jellyfish

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object dump extends AnyJellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input  :×:
    output :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (input.type  := input.Raw)  ::
    (output.type := output.Raw) ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    column      :×:
    tab         :×:
    lower_count :×:
    upper_count :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (column.type      := column.Raw)      ::
    (tab.type         := tab.Raw)         ::
    (lower_count.type := lower_count.Raw) ::
    (upper_count.type := upper_count.Raw) ::
    *[AnyDenotation]

  lazy val defaults = options(
    column(false)                     ::
    tab(false)                        ::
    lower_count(1: BigInt)            ::
    upper_count(BigInt("4294967295")) ::
    *[AnyDenotation]
  )

}

```




[test/scala/Jellyfish.scala]: ../../../../test/scala/Jellyfish.scala.md
[main/scala/api/options.scala]: ../options.scala.md
[main/scala/api/package.scala]: ../package.scala.md
[main/scala/api/expressions.scala]: ../expressions.scala.md
[main/scala/api/commands/histo.scala]: histo.scala.md
[main/scala/api/commands/queryAll.scala]: queryAll.scala.md
[main/scala/api/commands/query.scala]: query.scala.md
[main/scala/api/commands/dump.scala]: dump.scala.md
[main/scala/api/commands/bc.scala]: bc.scala.md
[main/scala/api/commands/count.scala]: count.scala.md