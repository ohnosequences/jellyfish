
```scala
package ohnosequences.jellyfish

import ohnosequences.cosas._, types._, records._, fns._, klists._

sealed trait AnyJellyfishCommand {

  lazy val name: Seq[String] = Seq("jellyfish", toString)

  type Arguments <: AnyRecordType { type Keys <: AnyProductType { type Types <: AnyKList { type Bound <: AnyJellyfishOption } } }
  type Options   <: AnyRecordType { type Keys <: AnyProductType { type Types <: AnyKList { type Bound <: AnyJellyfishOption } } }
}
abstract class JellyfishCommand extends AnyJellyfishCommand

trait AnyJellyfishOption extends AnyType {

  lazy val cmdName: String = toString replace("_", "-")
  // this is what is used for generating the Seq[String] cmd
  lazy val label: String = s"--${cmdName}"

  val valueToCmd: Raw => Seq[String]
}

abstract class JellyfishOption[V](val valueToCmd: V => Seq[String]) extends AnyJellyfishOption { type Raw = V }

case object AnyJellyfishOption {
  type is[FO <: AnyJellyfishOption] = FO with AnyJellyfishOption { type Raw = FO#Raw }
}

trait DefaultOptionValueToSeq extends DepFn1[Any, Seq[String]] {

  implicit def default[FO <: AnyJellyfishOption, V <: FO#Raw](implicit
    option: FO with AnyJellyfishOption { type Raw = FO#Raw }
  )
  : AnyApp1At[optionValueToSeq.type, FO := V] { type Y = Seq[String] }=
    App1 { v: FO := V => Seq(option.label) ++ option.valueToCmd(v.value).filterNot(_.isEmpty) }
}
case object optionValueToSeq extends DefaultOptionValueToSeq {

  // special cases

  implicit def atInput[V <: input.Raw]: AnyApp1At[optionValueToSeq.type, input.type := V] { type Y  = Seq[String] } =
    App1 { v: input.type := V => input.valueToCmd(v.value) }

  implicit def atMers[V <: mers.Raw]: AnyApp1At[optionValueToSeq.type, mers.type := V]  { type Y = Seq[String] } =
    App1 { v: mers.type := V => mers.valueToCmd(v.value) }

  implicit def atBc[V <: bc.Raw]: AnyApp1At[optionValueToSeq.type, bc.type := V]  { type Y = Seq[String] } =
    App1 {
       v: bc.type := V => v.value match { case None => Seq(); case Some(f) => Seq(bc.label) ++ bc.valueToCmd(Some(f)) }
    }
}

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

```




[test/scala/Jellyfish.scala]: ../../test/scala/Jellyfish.scala.md
[main/scala/traits.scala]: traits.scala.md
[main/scala/jellyfish.scala]: jellyfish.scala.md