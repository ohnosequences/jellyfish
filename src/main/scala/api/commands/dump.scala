package ohnosequences.jellyfish.api.jellyfish

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object dump extends AnyJellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    input  :×:
    output :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (input.type  := input.Raw)  ::
    (output.type := output.Raw) ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    column      :×:
    tab         :×:
    lower_count :×:
    upper_count :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (column.type      := column.Raw)      ::
    (tab.type         := tab.Raw)         ::
    (lower_count.type := lower_count.Raw) ::
    (upper_count.type := upper_count.Raw) ::
    *[AnyDenotation]

  lazy val defaults = options(
    column(false)                     ::
    tab(false)                        ::
    lower_count(1: BigInt)            ::
    upper_count(BigInt("4294967295")) ::
    *[AnyDenotation]
  )

}
