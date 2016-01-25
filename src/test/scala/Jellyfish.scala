package ohnosequences.jellyfish.test

import org.scalatest.FunSuite

// TODO clean this
import ohnosequences.jellyfish._, jellyfish._
import better.files._
import ohnosequences.cosas._, types._, klists._
import sys.process._

class CommandGeneration extends FunSuite {

  test("can generate Jellyfish commands") {

    val reads       : File = File("reads.fasta")
    val readsCount  : File = File("reads.count")
    val readsHisto  : File = File("reads.histo")

    val countExpr = JellyfishExpression(jellyfish.count)(
      jellyfish.count.arguments(
        input(reads)        ::
        output(readsCount)  ::
        *[AnyDenotation]
      ),
      jellyfish.count.defaults
    )

    val histoExpr = JellyfishExpression(jellyfish.histo)(
      jellyfish.histo.arguments(
        input(readsCount)   ::
        output(readsHisto)  ::
        *[AnyDenotation]
      ),
      jellyfish.histo.defaults
    )

    // TODO do something with this
    countExpr.cmd.!!
    histoExpr.cmd.!!
  }
}
