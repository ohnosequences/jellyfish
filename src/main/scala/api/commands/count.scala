package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api.{ options => opt, _ }
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object count extends AnyJellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    opt.input :×:
    opt.output :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (opt.input.type := opt.input.Raw)    ::
    (opt.output.type := opt.output.Raw)  ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    opt.mer_len   :×:
    opt.canonical :×:
    opt.size      :×:
    opt.bc        :×:
    opt.threads   :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (opt.mer_len.type := opt.mer_len.Raw)      ::
    (opt.canonical.type := opt.canonical.Raw)  ::
    (opt.size.type := opt.size.Raw)            ::
    (opt.bc.type := opt.bc.Raw)                ::
    (opt.threads.type := opt.threads.Raw)      ::
    *[AnyDenotation]

  lazy val defaults = options(
    opt.mer_len(24)             ::
    opt.canonical(true)         ::
    opt.size(100000000: BigInt) ::
    opt.bc(None: Option[File])  ::
    opt.threads(1)              ::
    *[AnyDenotation]
  )

  def apply(
    argumentValues: ArgumentsVals,
    optionValues: OptionsVals
  )
  : JellyfishExpression[count.type, ArgumentsVals, OptionsVals] =
    JellyfishExpression(count)(arguments := argumentValues, options := optionValues)
}
