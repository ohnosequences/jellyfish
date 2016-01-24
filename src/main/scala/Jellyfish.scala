package ohnosequences.jellyfish

import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

case object jellyfish {

  case object count extends JellyfishCommand {

    type Arguments = arguments.type
    case object arguments extends RecordType(input :×: output :×: |[AnyJellyfishOption])

    type Options = options.type
    case object options extends RecordType(
      mer_len       :×:
      canonical     :×:
      size          :×:
      threads       :×: |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      mer_len(24)       ::
      canonical(true)   ::
      size(100000000)   ::
      threads(1)        :: *[AnyDenotation]
    )
  }


  case object mer_len   extends JellyfishOption[Int](x => Seq(x.toString))
  case object size      extends JellyfishOption[Int](x => Seq(x.toString))
  case object threads   extends JellyfishOption[Int](x => Seq(x.toString))
  case object canonical extends JellyfishOption[Boolean](x => Seq())

  case object input     extends JellyfishOption[File](x => Seq(x.path.toString))
  case object output    extends JellyfishOption[File](x => Seq(x.path.toString))

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

    implicit def atInput[V <: input.Raw]: AnyApp1At[optionValueToSeq.type, input.type := V] { type Y  = Seq[String] } =
      App1 { v: input.type := V => input.valueToCmd(v.value) }

    // implicit def atOutput[V <: output.Raw]: AnyApp1At[optionValueToSeq.type, output.type := V]  { type Y = Seq[String] } =
    //   App1 { v: output.type := V => output.valueToCmd(v.value) }
  }

  trait AnyJellyfishExpression {

    type Command <: AnyJellyfishCommand
    val command: Command

    type ValArgs <: Command#Arguments#Raw
    type ValOpt <: Command#Options#Raw

    val argumentValues: Command#Arguments := ValArgs
    val optionValues: Command#Options := ValOpt
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

  implicit def jellyfishExpressionOps[FE <: AnyJellyfishExpression](expr: FE): JellyfishExpressionOps[FE] =
    JellyfishExpressionOps(expr)
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

}
