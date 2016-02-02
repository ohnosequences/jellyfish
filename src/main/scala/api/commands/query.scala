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

  type ArgumentsVals =
    (input.type := input.Raw)   ::
    (mers.type := mers.Raw)     ::
    (output.type := output.Raw) ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    load      :×:
    no_load   :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (load.type := load.Raw)       ::
    (no_load.type := no_load.Raw) ::
    *[AnyDenotation]

  lazy val defaults = options(
    load(false)     ::
    no_load(false)  ::
    *[AnyDenotation]
  )

  def apply(
    argumentValues: ArgumentsVals,
    optionValues: OptionsVals
  )
  : JellyfishExpression[query.type, ArgumentsVals, OptionsVals] =
    JellyfishExpression(query)(arguments := argumentValues, options := optionValues)
}
