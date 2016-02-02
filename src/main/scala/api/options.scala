package ohnosequences.jellyfish.api

import options._
import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

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

  implicit def atBools[O <: AnyJellyfishOption { type Raw = Boolean }]:
    AnyApp1At[optionValueToSeq.type, O := Boolean] { type Y = Seq[String] } =
    App1 { opt: O := Boolean => if (opt.value) Seq(opt.tpe.label) else Seq() }

}


case object options {

  case object mers        extends JellyfishOption[Seq[String]](x => x) // NOTE: funny option for input of cmds
  case object sequence    extends JellyfishOption[File](x => Seq(x.path.toString))
  case object load        extends JellyfishOption[Boolean](x => Seq())
  case object no_load     extends JellyfishOption[Boolean](x => Seq())
  case object input       extends JellyfishOption[File](x => Seq(x.path.toString))
  case object output      extends JellyfishOption[File](x => Seq(x.path.toString))
  case object mer_len     extends JellyfishOption[Int](x => Seq(x.toString))
  case object size        extends JellyfishOption[BigInt](x => Seq(x.toString))
  case object threads     extends JellyfishOption[Int](x => Seq(x.toString))
  case object canonical   extends JellyfishOption[Boolean](x => Seq())
  case object column      extends JellyfishOption[Boolean](x => Seq())
  case object tab         extends JellyfishOption[Boolean](x => Seq())
  case object lower_count extends JellyfishOption[BigInt](x => Seq(x.toString))
  case object upper_count extends JellyfishOption[BigInt](x => Seq(x.toString))
  case object low         extends JellyfishOption[BigInt](x => Seq(x.toString)) // (1)
  case object high        extends JellyfishOption[BigInt](x => Seq(x.toString)) // (10000)
  case object increment   extends JellyfishOption[BigInt](x => Seq(x.toString)) //  Increment value for buckets (1)
  case object full        extends JellyfishOption[Boolean](x => Seq()) // Don't skip count 0. (false)
  case object fpr         extends JellyfishOption[Double](x => Seq(x.toString))
  case object bc          extends JellyfishOption[Option[File]]({
    case None => Seq()
    case Some(f) => Seq(f.path.toString)
  })

}
