package ohnosequences.jellyfish

import ohnosequences.cosas._, types._, records._, klists._

package object api {

  type AnyJellyfishOptionsRecord =
    AnyRecordType {
      type Keys <: AnyProductType {
        type Types <: AnyKList.Of[AnyJellyfishOption]
      }
    }

  implicit def jellyfishOptionsOps[L <: AnyKList.withBound[AnyDenotation]](l: L):
    JellyfishOptionsOps[L] =
    JellyfishOptionsOps[L](l)

  // just an alias
  type uint64 = BigInt
  case object uint64 {
    val MinValue: uint64 = BigInt(0)
    // FIXME: this is not a max value of uint64:
    val MaxValue: uint64 = BigInt("4294967295")
  }

}

case class JellyfishOptionsOps[L <: AnyKList.withBound[AnyDenotation]](val l: L) extends AnyVal {

  def toSeq(implicit opsToSeq: api.JellyfishOptionsToSeq[L]): Seq[String] = opsToSeq(l)
}
