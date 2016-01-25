package ohnosequences.test

import org.scalatest.FunSuite

// TODO clean this
import ohnosequences.jellyfish._
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

    val countExpr = JellyfishExpression(count)(
      count.arguments(
        input(reads)        ::
        output(readsCount)  ::
        *[AnyDenotation]
      ),
      count.defaults update mer_len(4)
    )

    val histoExpr = JellyfishExpression(histo)(
      histo.arguments(
        input(readsCount)   ::
        output(readsHisto)  ::
        *[AnyDenotation]
      ),
      histo.defaults
    )

    val queryExpr = JellyfishExpression(query)(
      query.arguments(
        input(readsCount) ::
        mers(Seq("ATCT", "AATC", "TTAT", "ATCG")) ::
        output(mersQuery) ::
        *[AnyDenotation]
      ),
      query.defaults
    )

    val queryAllExpr = JellyfishExpression(queryAll)(
      queryAll.arguments(
        input(readsCount) ::
        sequence(fastaQ)  ::
        output(readQuery) ::
        *[AnyDenotation]
      ),
      queryAll.defaults
    )

    // TODO do something with this
    countExpr.cmd.!
    histoExpr.cmd.!
    queryExpr.cmd.!
    queryAllExpr.cmd.!
  }
}
