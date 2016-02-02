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

  type Options = options.type
  case object options extends RecordType(
    low       :×:
    high      :×:
    threads   :×:
    increment :×:
    full      :×: |[AnyJellyfishOption]
  )

  lazy val defaults = options(
    low(1L)       ::
    high(10000L)  ::
    threads(1)    ::
    increment(1L) ::
    full(false)   :: *[AnyDenotation]
  )
}
