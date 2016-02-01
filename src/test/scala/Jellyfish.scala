package ohnosequences.test

import org.scalatest.FunSuite

// TODO clean this
import ohnosequences.jellyfish._
import better.files._
import ohnosequences.cosas._, types._, klists._
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

  val countExpr = JellyfishExpression(jellyfish.count)(
    jellyfish.count.arguments(
      input(reads)        ::
      output(readsCount)  ::
      *[AnyDenotation]
    ),
    jellyfish.count.defaults update mer_len(4)
  )

  val bcExpr = JellyfishExpression(jellyfish.bc)(
    jellyfish.bc.arguments(
      input(reads)        ::
      output(readsBloom)  ::
      *[AnyDenotation]
    ),
    jellyfish.bc.defaults
  )

  val countAgainExpr = JellyfishExpression(jellyfish.count)(
    jellyfish.count.arguments(
      input(reads)        ::
      output(readsCount)  ::
      *[AnyDenotation]
    ),
    jellyfish.count.defaults update (mer_len(4) :: bc(Some(readsBloom) : Option[File]) :: *[AnyDenotation])
  )

  val histoExpr = JellyfishExpression(jellyfish.histo)(
    jellyfish.histo.arguments(
      input(readsCount)   ::
      output(readsHisto)  ::
      *[AnyDenotation]
    ),
    jellyfish.histo.defaults
  )

  val dumpExpr = JellyfishExpression(jellyfish.dump)(
    jellyfish.dump.arguments(
      input(readsCount) ::
      output(readsDump) ::
      *[AnyDenotation]
    ),
    jellyfish.dump.defaults
  )

  val queryExpr = JellyfishExpression(jellyfish.query)(
    jellyfish.query.arguments(
      input(readsCount) ::
      mers(Seq("ATCT", "AATC", "TTAT", "ATCG")) ::
      output(mersQuery) ::
      *[AnyDenotation]
    ),
    jellyfish.query.defaults
  )

  val queryAllExpr = JellyfishExpression(jellyfish.queryAll)(
    jellyfish.queryAll.arguments(
      input(readsCount) ::
      sequence(fastaQ)  ::
      output(readQuery) ::
      *[AnyDenotation]
    ),
    jellyfish.queryAll.defaults
  )

}

class CommandGeneration extends FunSuite {
  import testContext._

  // TODO do something with this
  test("jellyfish count") {
    assert { countExpr.cmd.! == 0 }
  }

  test("jellyfish histo") {
    assert { histoExpr.cmd.! == 0 }
  }

  test("jellyfish dump") {
    assert { dumpExpr.cmd.! == 0 }
  }

  test("jellyfish query") {
    assert { queryExpr.cmd.! == 0 }
  }

  test("jellyfish queryAll") {
    assert { queryAllExpr.cmd.! == 0 }
  }
}
