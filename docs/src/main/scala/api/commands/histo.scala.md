
```scala
package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api._, options._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object histo extends JellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input   :×:
    output  :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (input.type := input.Raw)    ::
    (output.type := output.Raw)  ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    low       :×:
    high      :×:
    threads   :×:
    increment :×:
    full      :×: |[AnyJellyfishOption]
  )

  type OptionsVals =
    (low.type := low.Raw)             ::
    (high.type := high.Raw)           ::
    (threads.type := threads.Raw)     ::
    (increment.type := increment.Raw) ::
    (full.type := full.Raw)           ::
    *[AnyDenotation]

  lazy val defaults = options(
    low(1: BigInt)        ::
    high(10000: BigInt)   ::
    threads(1)            ::
    increment(1: BigInt)  ::
    full(false)           ::
    *[AnyDenotation]
  )

  def apply(
    argumentValues: ArgumentsVals,
    optionValues: OptionsVals
  )
  : JellyfishExpression[histo.type, ArgumentsVals, OptionsVals] =
    JellyfishExpression(histo)(arguments := argumentValues, options := optionValues)
}

```




[test/scala/Jellyfish.scala]: ../../../../test/scala/Jellyfish.scala.md
[main/scala/api/options.scala]: ../options.scala.md
[main/scala/api/expressions.scala]: ../expressions.scala.md
[main/scala/api/commands/histo.scala]: histo.scala.md
[main/scala/api/commands/queryAll.scala]: queryAll.scala.md
[main/scala/api/commands/query.scala]: query.scala.md
[main/scala/api/commands/dump.scala]: dump.scala.md
[main/scala/api/commands/bc.scala]: bc.scala.md
[main/scala/api/commands/count.scala]: count.scala.md