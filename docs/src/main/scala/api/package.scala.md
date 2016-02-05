
```scala
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

```




[test/scala/Jellyfish.scala]: ../../../test/scala/Jellyfish.scala.md
[main/scala/api/options.scala]: options.scala.md
[main/scala/api/package.scala]: package.scala.md
[main/scala/api/expressions.scala]: expressions.scala.md
[main/scala/api/commands/histo.scala]: commands/histo.scala.md
[main/scala/api/commands/queryAll.scala]: commands/queryAll.scala.md
[main/scala/api/commands/query.scala]: commands/query.scala.md
[main/scala/api/commands/dump.scala]: commands/dump.scala.md
[main/scala/api/commands/bc.scala]: commands/bc.scala.md
[main/scala/api/commands/count.scala]: commands/count.scala.md