package ohnosequences.jellyfish.api.test

import org.scalatest.FunSuite

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, klists._

import better.files._
import sys.process._


case object testContext {

  def resourceFile(fileName: String): File =
    file"src/test/resources/${fileName}"

  lazy val reads:      File = resourceFile("reads.fasta")
  lazy val readsBloom: File = resourceFile("reads.bloom")
  lazy val readsCount: File = resourceFile("reads.count")
  lazy val readsCountM: File = resourceFile("reads.merged.count")
  lazy val readsHisto: File = resourceFile("reads.histo")
  lazy val readsDump:  File = resourceFile("reads.dump")
  lazy val mersQuery:  File = resourceFile("mers.query")
  lazy val fastaQ:     File = resourceFile("query.fasta")
  lazy val readQuery:  File = resourceFile("reads.query")


  lazy val countExpr = jellyfish.count(
    input(reads)        ::
    output(readsCount)  ::
    mer_len(4)          ::
    size(uint64(1000))  ::
    *[AnyDenotation],
    jellyfish.count.defaults.value
  )

  lazy val bcExpr = jellyfish.bc(
    input(reads)        ::
    output(readsBloom)  ::
    mer_len(4)          ::
    size(uint64(1000))  ::
    *[AnyDenotation],
    jellyfish.bc.defaults.value
  )

  lazy val countAgainExpr = jellyfish.count(
    input(reads)        ::
    output(readsCount)  ::
    mer_len(4)          ::
    size(uint64(1000))  ::
    *[AnyDenotation],
    jellyfish.count.defaults.update( opt.bc(Some(readsBloom) : Option[File])).value
  )

  lazy val histoExpr = jellyfish.histo(
      input(readsCount)   ::
      output(readsHisto)  ::
      *[AnyDenotation],
    jellyfish.histo.defaults.value
  )

  lazy val dumpExpr = jellyfish.dump(
      input(readsCount) ::
      output(readsDump) ::
      *[AnyDenotation],
    jellyfish.dump.defaults.value
  )

  lazy val queryExpr = jellyfish.query(
      input(readsCount) ::
      mers(Seq("ATCT", "AATC", "TTAT", "ATCG")) ::
      output(mersQuery) ::
      *[AnyDenotation],
    jellyfish.query.defaults.value
  )

  lazy val queryAllExpr = jellyfish.queryAll(
      input(readsCount) ::
      sequence(fastaQ)  ::
      output(readQuery) ::
      *[AnyDenotation],
    jellyfish.queryAll.defaults.value
  )

  lazy val mergeExpr = jellyfish.merge(
      inputs(Seq(readsCount, readsCount)) ::
      output(readsCountM) ::
      *[AnyDenotation],
    jellyfish.merge.defaults.value
  )

}

class CommandGeneration extends FunSuite {
  import testContext._

  // TODO do something with this
  test("jellyfish count") {
    countExpr.toSeq.foreach{ info(_) }
    assert { countExpr.toSeq.! == 0 }
  }

  test("jellyfish histo") {
    histoExpr.toSeq.foreach{ info(_) }
    assert { histoExpr.toSeq.! == 0 }
  }

  test("jellyfish dump") {
    dumpExpr.toSeq.foreach{ info(_) }
    assert { dumpExpr.toSeq.! == 0 }
  }

  test("jellyfish query") {
    queryExpr.toSeq.foreach{ info(_) }
    assert { queryExpr.toSeq.! == 0 }
  }

  test("jellyfish queryAll") {
    queryAllExpr.toSeq.foreach{ info(_) }
    assert { queryAllExpr.toSeq.! == 0 }
  }

  test("jellyfish merge") {
    mergeExpr.toSeq.foreach{ info(_) }
    assert { mergeExpr.toSeq.! == 0 }
  }
}
