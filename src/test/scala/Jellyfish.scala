package ohnosequences.jellyfish.api.test

import org.scalatest.FunSuite

import ohnosequences.jellyfish.api._, opt._
import ohnosequences.cosas._, types._, klists._

import better.files._
import sys.process._


case object testContext {

  val reads      : File = File("reads.fasta")
  val readsBloom : File = File("reads.bloom")
  val readsCount : File = File("reads.count")
  val readsHisto : File = File("reads.histo")
  val readsDump  : File = File("reads.dump")
  val mersQuery  : File = File("mers.query")
  val fastaQ     : File = File("query.fasta")
  val readQuery  : File = File("reads.query")

  val countExpr = jellyfish.count(
    input(reads)        ::
    output(readsCount)  ::
    *[AnyDenotation],
    (jellyfish.count.defaults update opt.mer_len(4)).value
  )

  val bcExpr = jellyfish.bc(
    input(reads)        ::
    output(readsBloom)  ::
    *[AnyDenotation],
    jellyfish.bc.defaults.value
  )

  val countAgainExpr = jellyfish.count(
    input(reads)        ::
    output(readsCount)  ::
    *[AnyDenotation],
    jellyfish.count.defaults.update(
      opt.mer_len(4) ::
      opt.bc(Some(readsBloom) : Option[File]) ::
      *[AnyDenotation]
    ).value
  )

  val histoExpr = jellyfish.histo(
      input(readsCount)   ::
      output(readsHisto)  ::
      *[AnyDenotation],
    jellyfish.histo.defaults.value
  )

  val dumpExpr = jellyfish.dump(
      input(readsCount) ::
      output(readsDump) ::
      *[AnyDenotation],
    jellyfish.dump.defaults.value
  )

  val queryExpr = jellyfish.query(
      input(readsCount) ::
      mers(Seq("ATCT", "AATC", "TTAT", "ATCG")) ::
      output(mersQuery) ::
      *[AnyDenotation],
    jellyfish.query.defaults.value
  )

  val queryAllExpr = jellyfish.queryAll(
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
