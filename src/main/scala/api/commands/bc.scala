package ohnosequences.jellyfish.api.commands

import ohnosequences.jellyfish.api._, options._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object bc extends JellyfishCommand {

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
    mer_len   :×:
    canonical :×:
    fpr       :×:
    size      :×:
    threads   :×: |[AnyJellyfishOption]
  )

  type OptionsVals =
    (mer_len.type := mer_len.Raw)     ::
    (canonical.type := canonical.Raw) ::
    (fpr.type := fpr.Raw)             ::
    (size.type := size.Raw)           ::
    (threads.type := threads.Raw)     ::
    *[AnyDenotation]

  lazy val defaults = options(
    mer_len(24)             ::
    canonical(true)         ::
    fpr(0.001)              ::
    size(100000000: BigInt) ::
    threads(1)              :: *[AnyDenotation]
  )

  def apply(
    argumentValues: ArgumentsVals,
    optionValues: OptionsVals
  )
  : JellyfishExpression[bc.type, ArgumentsVals, OptionsVals] =
    JellyfishExpression(bc)(arguments := argumentValues, options := optionValues)
}
