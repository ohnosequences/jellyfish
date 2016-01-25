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
    val mersQuery   : File = File("mers.query")
    val fastaQ      : File = File("query.fasta")
    val readQuery   : File = File("reads.query")

    val countExpr = JellyfishExpression(jellyfish.count)(
      jellyfish.count.arguments(
        input(reads)        ::
        output(readsCount)  ::
        *[AnyDenotation]
      ),
      jellyfish.count.defaults update mer_len(4)
    )

    val histoExpr = JellyfishExpression(jellyfish.histo)(
      jellyfish.histo.arguments(
        input(readsCount)   ::
        output(readsHisto)  ::
        *[AnyDenotation]
      ),
      jellyfish.histo.defaults
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

    // TODO do something with this
    countExpr.cmd.!
    histoExpr.cmd.!
    queryExpr.cmd.!
    queryAllExpr.cmd.!
  }
}
