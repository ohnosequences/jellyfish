package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api._, options._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object bc extends JellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input :×:
    output :×:
    |[AnyJellyfishOption]
  )

  type Options = options.type
  case object options extends RecordType(
    mer_len   :×:
    canonical :×:
    fpr       :×:
    size      :×:
    threads   :×: |[AnyJellyfishOption]
  )

  lazy val defaults = options(
    mer_len(24)       ::
    canonical(true)   ::
    fpr(0.001)        ::
    size(100000000L)  ::
    threads(1)        :: *[AnyDenotation]
  )
}
