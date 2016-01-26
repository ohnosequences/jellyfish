package ohnosequences.jellyfish

import ohnosequences.cosas._, types._, records._, fns._, klists._
import better.files._

// NOTE just write jellyfish.cmd after importing ohnosequences.jellyfish._
case object jellyfish {

  case object count extends JellyfishCommand {

    type Arguments = arguments.type
    case object arguments extends RecordType(input :×: output :×: |[AnyJellyfishOption])

    type Options = options.type
    case object options extends RecordType(
      mer_len       :×:
      canonical     :×:
      size          :×:
      ohnosequences.jellyfish.bc :×:
      threads       :×: |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      mer_len(24)       ::
      canonical(true)   ::
      size(100000000L)  ::
      ohnosequences.jellyfish.bc(None: Option[File]) ::
      threads(1)        :: *[AnyDenotation]
    )
  }

  case object bc extends JellyfishCommand {

    type Arguments = arguments.type
    case object arguments extends RecordType(input :×: output :×: |[AnyJellyfishOption])

    type Options = options.type
    case object options extends RecordType(
      mer_len       :×:
      canonical     :×:
      fpr           :×:
      size          :×:
      threads       :×: |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      mer_len(24)       ::
      canonical(true)   ::
      fpr(0.001)        ::
      size(100000000L)  ::
      threads(1)        :: *[AnyDenotation]
    )
  }

  case object dump extends JellyfishCommand {

    type Arguments = arguments.type
    case object arguments extends RecordType(input :×: output :×: |[AnyJellyfishOption])

    type Options = options.type
    case object options extends RecordType(
      columnar    :×:
      tab         :×:
      lower_count :×:
      upper_count :×: |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      columnar(true)              ::
      tab(true)                   ::
      lower_count(1L)             ::
      // 4 bytes counter size is twice this but...
      upper_count(Long.MaxValue)  :: *[AnyDenotation]
    )
  }

  case object histo extends JellyfishCommand {

    type Arguments = arguments.type
    case object arguments extends RecordType(
      input   :×:
      output  :×:
      |[AnyJellyfishOption]
    )

    type Options = options.type
    case object options extends RecordType(
      low       :×:
      high      :×:
      threads   :×:
      increment :×:
      full      :×: |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      low(1L)       ::
      high(10000L)  ::
      threads(1)    ::
      increment(1L) ::
      full(false)   :: *[AnyDenotation]
    )
  }

  case object query extends JellyfishCommand {

    type Arguments = arguments.type
    case object arguments extends RecordType(
      input   :×:
      mers    :×:
      output  :×:
      |[AnyJellyfishOption]
    )

    type Options = options.type
    case object options extends RecordType(
      load      :×:
      no_load   :×:
      |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      load(false)     ::
      no_load(false)  ::
      *[AnyDenotation]
    )
  }

  case object queryAll extends JellyfishCommand {

    override lazy val name = Seq("jellyfish", "query")

    type Arguments = arguments.type
    case object arguments extends RecordType(
      input     :×:
      sequence  :×:
      output    :×:
      |[AnyJellyfishOption]
    )

    type Options = options.type
    case object options extends RecordType(
      load      :×:
      no_load   :×:
      |[AnyJellyfishOption]
    )

    lazy val defaults = options(
      load(false)     ::
      no_load(false)  ::
      *[AnyDenotation]
    )
  }
}



case object mers        extends JellyfishOption[Seq[String]](x => x) // NOTE: funny option for input of cmds
case object sequence    extends JellyfishOption[File](x => Seq(x.path.toString))
case object load        extends JellyfishOption[Boolean](x => Seq())
case object no_load     extends JellyfishOption[Boolean](x => Seq())
case object input       extends JellyfishOption[File](x => Seq(x.path.toString))
case object output      extends JellyfishOption[File](x => Seq(x.path.toString))
case object mer_len     extends JellyfishOption[Int](x => Seq(x.toString))
case object size        extends JellyfishOption[Long](x => Seq(x.toString))
case object threads     extends JellyfishOption[Int](x => Seq(x.toString))
case object canonical   extends JellyfishOption[Boolean](x => Seq())
case object columnar    extends JellyfishOption[Boolean](x => Seq())
case object tab         extends JellyfishOption[Boolean](x => Seq())
case object lower_count extends JellyfishOption[Long](x => Seq(toString))
case object upper_count extends JellyfishOption[Long](x => Seq(toString))
case object low         extends JellyfishOption[Long](x => Seq(x.toString)) // (1)
case object high        extends JellyfishOption[Long](x => Seq(x.toString)) // (10000)
case object increment   extends JellyfishOption[Long](x => Seq(x.toString)) //  Increment value for buckets (1)
case object full        extends JellyfishOption[Boolean](x => Seq()) // Don't skip count 0. (false)
case object fpr         extends JellyfishOption[Double](x => Seq(x.toString))
case object bc          extends JellyfishOption[Option[File]](
  x => x match { case None => Seq(); case Some(f) => Seq(f.path.toString)}
)
