package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api.{ options => opt, _ }
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object count extends JellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    opt.input :×:
    opt.output :×:
    |[AnyJellyfishOption]
  )

  type Options = options.type
  case object options extends RecordType(
    opt.mer_len   :×:
    opt.canonical :×:
    opt.size      :×:
    opt.bc :×:
    opt.threads   :×:
    |[AnyJellyfishOption]
  )

  lazy val defaults = options(
    opt.mer_len(24) ::
    opt.canonical(true) ::
    opt.size(100000000L) ::
    opt.bc(None: Option[File]) ::
    opt.threads(1) ::
    *[AnyDenotation]
  )
}
