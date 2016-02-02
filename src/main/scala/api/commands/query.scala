package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api._, options._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object query extends JellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input   :×:
    mers    :×:
    output  :×:
    |[AnyJellyfishOption]
  )

  type Options = options.type
  case object options extends RecordType(
    load      :×:
    no_load   :×:
    |[AnyJellyfishOption]
  )

  lazy val defaults = options(
    load(false)     ::
    no_load(false)  ::
    *[AnyDenotation]
  )
}
