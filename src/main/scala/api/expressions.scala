package ohnosequences.jellyfish.api

import ohnosequences.cosas._, types._

trait AnyJellyfishCommand { cmd =>

  lazy val name: Seq[String] = Seq("jellyfish", cmd.toString)

  type Arguments <: AnyJellyfishOptionsRecord
  type Options   <: AnyJellyfishOptionsRecord

  type ArgumentsVals <: Arguments#Raw
  type OptionsVals   <: Options#Raw

  /* default values for options; they are *optional*, so should have default values. */
  val defaults: Options := OptionsVals
}

case object AnyJellyfishCommand {

  implicit def jellyfishCommandOps[Cmd <: AnyJellyfishCommand](cmd: Cmd):
    JellyfishCommandOps[Cmd] =
    JellyfishCommandOps[Cmd](cmd)
}

case class JellyfishCommandOps[Cmd <: AnyJellyfishCommand](cmd: Cmd) extends AnyVal {

  def apply(
    argumentValues: Cmd#ArgumentsVals,
    optionValues: Cmd#OptionsVals
  )(implicit
    argValsToSeq: JellyfishOptionsToSeq[Cmd#ArgumentsVals],
    optValsToSeq: JellyfishOptionsToSeq[Cmd#OptionsVals]
  ): JellyfishExpression[Cmd] =
     JellyfishExpression(cmd)(argumentValues, optionValues)(argValsToSeq, optValsToSeq)
}


trait AnyJellyfishExpression {

  type Command <: AnyJellyfishCommand
  val  command: Command

  val argumentValues: Command#ArgumentsVals
  val optionValues: Command#OptionsVals

  // implicitly:
  val argValsToSeq: JellyfishOptionsToSeq[Command#ArgumentsVals]
  val optValsToSeq: JellyfishOptionsToSeq[Command#OptionsVals]

  def toSeq: Seq[String] =
    command.name ++
    argValsToSeq(argumentValues) ++
    optValsToSeq(optionValues)
}

case class JellyfishExpression[Cmd <: AnyJellyfishCommand](val command: Cmd)(
  val argumentValues: Cmd#ArgumentsVals,
  val optionValues: Cmd#OptionsVals
)(implicit
  val argValsToSeq: JellyfishOptionsToSeq[Cmd#ArgumentsVals],
  val optValsToSeq: JellyfishOptionsToSeq[Cmd#OptionsVals]
) extends AnyJellyfishExpression { type Command = Cmd }
