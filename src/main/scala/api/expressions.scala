package ohnosequences.jellyfish.api

import ohnosequences.cosas._, types._, records._, fns._, klists._

sealed trait AnyJellyfishCommand {

  lazy val name: Seq[String] = Seq("jellyfish", toString)

  type Arguments <: AnyRecordType { type Keys <: AnyProductType { type Types <: AnyKList { type Bound <: AnyJellyfishOption } } }
  type Options   <: AnyRecordType { type Keys <: AnyProductType { type Types <: AnyKList { type Bound <: AnyJellyfishOption } } }

  type ArgumentsVals <: Command#Arguments#Raw
  type OptionsVals   <: Command#Options#Raw
}


trait AnyJellyfishExpression {

  type Command <: AnyJellyfishCommand
  val  command: Command

  val argumentValues: Command#Arguments := Command#ArgumentsVals
  val optionValues: Command#Options := Command#OptionsVals
}

case class JellyfishExpression[Cmd <: AnyJellyfishCommand](val command: Cmd)(
  val argumentValues: Cmd#Arguments := Cmd#ArgumentsVals,
  val optionValues: Cmd#Options := Cmd#OptionsVals
// )(implicit
//   mapArgs: AnyApp2At[mapKList[optionValueToSeq.type, Seq[String]], optionValueToSeq.type, FE#ValArgs] { type Y = AO },
//   mapOpts: AnyApp2At[mapKList[optionValueToSeq.type, Seq[String]], optionValueToSeq.type, FE#ValOpt] { type Y = OO }
) extends AnyJellyfishExpression { type Command = Cmd }


case object AnyJellyfishExpression {

  implicit def jellyfishExpressionOps[FE <: AnyJellyfishExpression](expr: FE):
    JellyfishExpressionOps[FE] =
    JellyfishExpressionOps(expr)
}

case class JellyfishExpressionOps[FE <: AnyJellyfishExpression](val expr: FE) extends AnyVal {

  // def toSeq: Seq[String] = {
  //
  //   val (argsSeqs, optsSeqs): (List[Seq[String]], List[Seq[String]]) = (
  //     (expr.argumentValues.value: FE#ValArgs) map optionValueToSeq asList,
  //     (expr.optionValues.value: FE#ValOpt) map optionValueToSeq asList
  //   )
  //
  //   expr.command.name ++ argsSeqs.toSeq.flatten ++ optsSeqs.toSeq.flatten
  // }
}
