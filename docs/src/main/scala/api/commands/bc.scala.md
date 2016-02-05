
```scala
package ohnosequences.jellyfish.api.jellyfish

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object bc extends AnyJellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input   :×:
    output  :×:
    mer_len :×:
    size    :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (input.type   := input.Raw)    ::
    (output.type  := output.Raw)   ::
    (mer_len.type := mer_len.Raw)  ::
    (size.type    := size.Raw)     ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    canonical :×:
    fpr       :×:
    threads   :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (canonical.type  := canonical.Raw) ::
    (fpr.type        := fpr.Raw)       ::
    (threads.type    := threads.Raw)   ::
    *[AnyDenotation]

  lazy val defaults = options(
    canonical(false)        ::
    fpr(0.001)              ::
    threads(1)              ::
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