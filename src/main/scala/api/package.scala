package ohnosequences.jellyfish

import ohnosequences.cosas._, types._, records._, fns._, klists._, typeUnions._
// import better.files._

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

}

case class JellyfishOptionsOps[L <: AnyKList.withBound[AnyDenotation]](val l: L) extends AnyVal {

  def toSeq(implicit opsToSeq: api.JellyfishOptionsToSeq[L]): Seq[String] = opsToSeq(l)
}
