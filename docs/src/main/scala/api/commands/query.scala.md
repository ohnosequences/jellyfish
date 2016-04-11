
```scala
package ohnosequences.jellyfish.api.jellyfish

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object query extends AnyJellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input  :×:
    mers   :×:
    output :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (input.type      := input.Raw)  ::
    (mers.type       := mers.Raw)   ::
    (output.type     := output.Raw) ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    load    :×:
    no_load :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (load.type    := load.Raw)    ::
    (no_load.type := no_load.Raw) ::
    *[AnyDenotation]

  lazy val defaults = options(
    load(false)    ::
    no_load(false) ::
    *[AnyDenotation]
  )

}

```




[test/scala/Jellyfish.scala]: ../../../../test/scala/Jellyfish.scala.md
[main/scala/api/options.scala]: ../options.scala.md
[main/scala/api/package.scala]: ../package.scala.md
[main/scala/api/expressions.scala]: ../expressions.scala.md
[main/scala/api/uint64.scala]: ../uint64.scala.md
[main/scala/api/commands/histo.scala]: histo.scala.md
[main/scala/api/commands/queryAll.scala]: queryAll.scala.md
[main/scala/api/commands/query.scala]: query.scala.md
[main/scala/api/commands/dump.scala]: dump.scala.md
[main/scala/api/commands/merge.scala]: merge.scala.md
[main/scala/api/commands/bc.scala]: bc.scala.md
[main/scala/api/commands/count.scala]: count.scala.md