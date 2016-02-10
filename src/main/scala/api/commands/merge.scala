package ohnosequences.jellyfish.api.jellyfish

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

/* Merge jellyfish databases */
case object merge extends AnyJellyfishCommand {

  type Arguments = arguments.type
  case object arguments extends RecordType(
    inputs :×:
    output :×:
    |[AnyJellyfishOption]
  )

  type ArgumentsVals =
    (inputs.type := inputs.Raw) ::
    (output.type := output.Raw) ::
    *[AnyDenotation]

  type Options = options.type
  case object options extends RecordType(
    lower_count :×:
    upper_count :×:
    |[AnyJellyfishOption]
  )

  type OptionsVals =
    (lower_count.type := lower_count.Raw) ::
    (upper_count.type := upper_count.Raw) ::
    *[AnyDenotation]

  lazy val defaults = options(
    lower_count(uint64(1))       ::
    upper_count(uint64.MaxValue) ::
    *[AnyDenotation]
  )

}
