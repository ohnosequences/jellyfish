package ohnosequences.jellyfish.api.test

import org.scalatest.FunSuite

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, klists._

import better.files._
import sys.process._


case object testContext {

  def resourceFile(suffix: String): File =
    File(getClass.getResource(s"/${suffix}").getPath)

  lazy val reads:      File = resourceFile("reads.fasta")
  lazy val readsBloom: File = resourceFile("reads.bloom")
  lazy val readsCount: File = resourceFile("reads.count")
  lazy val readsHisto: File = resourceFile("reads.histo")
  lazy val readsDump:  File = resourceFile("reads.dump")
  lazy val mersQuery:  File = resourceFile("mers.query")
  lazy val fastaQ:     File = resourceFile("query.fasta")
  lazy val readQuery:  File = resourceFile("reads.query")


  lazy val countExpr = jellyfish.count(
    input(reads)        ::
    output(readsCount)  ::
    *[AnyDenotation],
    (jellyfish.count.defaults update opt.mer_len(4)).value
  )

  lazy val bcExpr = jellyfish.bc(
    input(reads)        ::
    output(readsBloom)  ::
    *[AnyDenotation],
    jellyfish.bc.defaults.value
  )

  lazy val countAgainExpr = jellyfish.count(
    input(reads)        ::
    output(readsCount)  ::
    *[AnyDenotation],
    jellyfish.count.defaults.update(
      opt.mer_len(4) ::
      opt.bc(Some(readsBloom) : Option[File]) ::
      *[AnyDenotation]
    ).value
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
}

class CommandGeneration extends FunSuite {
  import testContext._

  // TODO do something with this
  test("jellyfish count") {
    assert { countExpr.toSeq.! == 0 }
  }

  test("jellyfish histo") {
    assert { histoExpr.toSeq.! == 0 }
  }

  test("jellyfish dump") {
    assert { dumpExpr.toSeq.! == 0 }
  }

  test("jellyfish query") {
    assert { queryExpr.toSeq.! == 0 }
  }

  test("jellyfish queryAll") {
    assert { queryAllExpr.toSeq.! == 0 }
  }
}
