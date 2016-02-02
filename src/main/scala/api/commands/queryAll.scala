package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api._, options._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object queryAll extends JellyfishCommand {

  override lazy val name = Seq("jellyfish", "query")

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input     :×:
    sequence  :×:
    output    :×:
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
