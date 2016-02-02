package ohnosequences.jellyfish.api

import ohnosequences.cosas._, types._, records._, fns._, klists._

sealed trait AnyJellyfishCommand {

  lazy val name: Seq[String] = Seq("jellyfish", toString)

  type Arguments <: AnyRecordType { type Keys <: AnyProductType { type Types <: AnyKList { type Bound <: AnyJellyfishOption } } }
  type Options   <: AnyRecordType { type Keys <: AnyProductType { type Types <: AnyKList { type Bound <: AnyJellyfishOption } } }
}
abstract class JellyfishCommand extends AnyJellyfishCommand


trait AnyJellyfishExpression {

  type Command <: AnyJellyfishCommand
  val command: Command

  type ValArgs <: Command#Arguments#Raw
  type ValOpt <: Command#Options#Raw

  val argumentValues: Command#Arguments := ValArgs
  val optionValues: Command#Options := ValOpt
}

case object AnyJellyfishExpression {

  implicit def jellyfishExpressionOps[FE <: AnyJellyfishExpression](expr: FE): JellyfishExpressionOps[FE] =
    JellyfishExpressionOps(expr)
}

case class JellyfishExpression[
  Cmd <: AnyJellyfishCommand,
  AV <: Cmd#Arguments#Raw,
  OV <: Cmd#Options#Raw
](val command: Cmd)(
  val argumentValues: Cmd#Arguments := AV,
  val optionValues: Cmd#Options := OV
)
extends AnyJellyfishExpression {

  type Command = Cmd; type ValArgs = AV; type ValOpt = OV
}

case class JellyfishExpressionOps[FE <: AnyJellyfishExpression](val expr: FE) extends AnyVal {

  def cmd[
    AO <: AnyKList.withBound[Seq[String]],
    OO <: AnyKList.withBound[Seq[String]]
  ](implicit
    mapArgs: AnyApp2At[mapKList[optionValueToSeq.type, Seq[String]], optionValueToSeq.type, FE#ValArgs] { type Y = AO },
    mapOpts: AnyApp2At[mapKList[optionValueToSeq.type, Seq[String]], optionValueToSeq.type, FE#ValOpt] { type Y = OO }
  )
  : Seq[String] = {

    val (argsSeqs, optsSeqs): (List[Seq[String]], List[Seq[String]]) = (
      (expr.argumentValues.value: FE#ValArgs) map optionValueToSeq asList,
      (expr.optionValues.value: FE#ValOpt) map optionValueToSeq asList
    )

    expr.command.name ++ argsSeqs.toSeq.flatten ++ optsSeqs.toSeq.flatten
  }
}
