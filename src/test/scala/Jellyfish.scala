package ohnosequences.jellyfish.api.test

import org.scalatest.FunSuite

import ohnosequences.jellyfish.api._, options._, commands._
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

  val countExpr = JellyfishExpression(commands.count)(
    commands.count.arguments(
      input(reads)        ::
      output(readsCount)  ::
      *[AnyDenotation]
    ),
    commands.count.defaults update mer_len(4)
  )

  val bcExpr = JellyfishExpression(commands.bc)(
    commands.bc.arguments(
      input(reads)        ::
      output(readsBloom)  ::
      *[AnyDenotation]
    ),
    commands.bc.defaults
  )

  val countAgainExpr = JellyfishExpression(commands.count)(
    commands.count.arguments(
      input(reads)        ::
      output(readsCount)  ::
      *[AnyDenotation]
    ),
    commands.count.defaults update (mer_len(4) :: options.bc(Some(readsBloom) : Option[File]) :: *[AnyDenotation])
  )

  val histoExpr = JellyfishExpression(commands.histo)(
    commands.histo.arguments(
      input(readsCount)   ::
      output(readsHisto)  ::
      *[AnyDenotation]
    ),
    commands.histo.defaults
  )

  val dumpExpr = JellyfishExpression(commands.dump)(
    commands.dump.arguments(
      input(readsCount) ::
      output(readsDump) ::
      *[AnyDenotation]
    ),
    commands.dump.defaults
  )

  val queryExpr = JellyfishExpression(commands.query)(
    commands.query.arguments(
      input(readsCount) ::
      mers(Seq("ATCT", "AATC", "TTAT", "ATCG")) ::
      output(mersQuery) ::
      *[AnyDenotation]
    ),
    commands.query.defaults
  )

  val queryAllExpr = JellyfishExpression(commands.queryAll)(
    commands.queryAll.arguments(
      input(readsCount) ::
      sequence(fastaQ)  ::
      output(readQuery) ::
      *[AnyDenotation]
    ),
    commands.queryAll.defaults
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
