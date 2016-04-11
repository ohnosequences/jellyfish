
```scala
package ohnosequences.jellyfish.api

import ohnosequences.cosas._, types._, fns._, klists._
import better.files._

trait AnyJellyfishOption extends AnyType {

  lazy val cmdName: String = toString replace("_", "-")
  // this is what is used for generating the Seq[String] cmd
  lazy val label: String = s"--${cmdName}"

  val valueToCmd: Raw => Seq[String]
}

case object AnyJellyfishOption {

  implicit def jellyfishOptionOps[Opt <: AnyJellyfishOption](opt: Opt):
    JellyfishOptionOps[Opt] =
    JellyfishOptionOps[Opt](opt)
}

case class JellyfishOptionOps[Opt <: AnyJellyfishOption](opt: Opt) extends AnyVal {

  // NOTE: this apply gets rid of unnecessary covarience in Raw
  def apply(v: Opt#Raw): Opt := Opt#Raw =
    (opt: Opt) := v
}

abstract class JellyfishOption[V](val valueToCmd: V => Seq[String])
  extends AnyJellyfishOption { type Raw = V }


trait DefaultOptionValueToSeq extends DepFn1[Any, Seq[String]] {

  implicit def default[FO <: AnyJellyfishOption, V <: FO#Raw](implicit
    option: FO with AnyJellyfishOption { type Raw = FO#Raw }
  )
  : AnyApp1At[optionValueToSeq.type, FO := V] { type Y = Seq[String] }=
    App1 { v: FO := V => Seq(option.label) ++ option.valueToCmd(v.value).filterNot(_.isEmpty) }
}

case object optionValueToSeq extends DefaultOptionValueToSeq {

  // special cases

  implicit def atInput[V <: opt.input.Raw]: AnyApp1At[optionValueToSeq.type, opt.input.type := V] { type Y  = Seq[String] } =
    App1 { v: opt.input.type := V => opt.input.valueToCmd(v.value) }

  implicit def atInputs[V <: opt.inputs.Raw]: AnyApp1At[optionValueToSeq.type, opt.inputs.type := V]  { type Y = Seq[String] } =
    App1 { v: opt.inputs.type := V => opt.inputs.valueToCmd(v.value) }

  implicit def atMers[V <: opt.mers.Raw]: AnyApp1At[optionValueToSeq.type, opt.mers.type := V]  { type Y = Seq[String] } =
    App1 { v: opt.mers.type := V => opt.mers.valueToCmd(v.value) }

  implicit def atBc[V <: opt.bc.Raw]: AnyApp1At[optionValueToSeq.type, opt.bc.type := V]  { type Y = Seq[String] } =
    App1 {
       v: opt.bc.type := V => v.value match {
         case None => Seq()
         case Some(f) => Seq(opt.bc.label) ++ opt.bc.valueToCmd(Some(f))
       }
    }

  implicit def atBools[O <: AnyJellyfishOption { type Raw = Boolean }]:
    AnyApp1At[optionValueToSeq.type, O := Boolean] { type Y = Seq[String] } =
    App1 { opt: O := Boolean => if (opt.value) Seq(opt.tpe.label) else Seq() }

}
```

This works as a type class, which provides a way of serializing a list of AnyJellyfishOption's

```scala
trait JellyfishOptionsToSeq[L <: AnyKList.withBound[AnyDenotation]] {

  def apply(l: L): Seq[String]
}

case object JellyfishOptionsToSeq {

  implicit def default[L <: AnyKList.withBound[AnyDenotation], O <: AnyKList.withBound[Seq[String]]](implicit
    mapp: AnyApp2At[mapKList[optionValueToSeq.type, Seq[String]], optionValueToSeq.type, L] { type Y = O }
  ): JellyfishOptionsToSeq[L] = new JellyfishOptionsToSeq[L] {
      def apply(l: L): Seq[String] = mapp(optionValueToSeq, l).asList.flatten
  }
}


case object opt {

  case object mers        extends JellyfishOption[Seq[String]](x => x) // NOTE: funny option for input of cmds
  case object sequence    extends JellyfishOption[File](x => Seq(x.path.toString))
  case object load        extends JellyfishOption[Boolean](x => Seq())
  case object no_load     extends JellyfishOption[Boolean](x => Seq())
  case object input       extends JellyfishOption[File](x => Seq(x.path.toString))
  case object inputs      extends JellyfishOption[Seq[File]](x => x.map{ _.path.toString })
  case object output      extends JellyfishOption[File](x => Seq(x.path.toString))
  case object mer_len     extends JellyfishOption[Int](x => Seq(x.toString))
  case object size        extends JellyfishOption[uint64](x => Seq(x.toString))
  case object threads     extends JellyfishOption[Int](x => Seq(x.toString))
  case object canonical   extends JellyfishOption[Boolean](x => Seq())
  case object column      extends JellyfishOption[Boolean](x => Seq())
  case object tab         extends JellyfishOption[Boolean](x => Seq())
  case object lower_count extends JellyfishOption[uint64](x => Seq(x.toString))
  case object upper_count extends JellyfishOption[uint64](x => Seq(x.toString))
  case object low         extends JellyfishOption[uint64](x => Seq(x.toString)) // (1)
  case object high        extends JellyfishOption[uint64](x => Seq(x.toString)) // (10000)
  case object increment   extends JellyfishOption[uint64](x => Seq(x.toString)) //  Increment value for buckets (1)
  case object full        extends JellyfishOption[Boolean](x => Seq()) // Don't skip count 0. (false)
  case object fpr         extends JellyfishOption[Double](x => Seq(x.toString))
  case object bc          extends JellyfishOption[Option[File]]({
    case None => Seq()
    case Some(f) => Seq(f.path.toString)
  })

}

```




[test/scala/Jellyfish.scala]: ../../../test/scala/Jellyfish.scala.md
[main/scala/api/options.scala]: options.scala.md
[main/scala/api/package.scala]: package.scala.md
[main/scala/api/expressions.scala]: expressions.scala.md
[main/scala/api/uint64.scala]: uint64.scala.md
[main/scala/api/commands/histo.scala]: commands/histo.scala.md
[main/scala/api/commands/queryAll.scala]: commands/queryAll.scala.md
[main/scala/api/commands/query.scala]: commands/query.scala.md
[main/scala/api/commands/dump.scala]: commands/dump.scala.md
[main/scala/api/commands/merge.scala]: commands/merge.scala.md
[main/scala/api/commands/bc.scala]: commands/bc.scala.md
[main/scala/api/commands/count.scala]: commands/count.scala.md