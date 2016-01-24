package ohnosequences.jellyfish.test

import org.scalatest.FunSuite

// TODO clean this
import ohnosequences.jellyfish._, jellyfish._
import better.files._
import ohnosequences.cosas._, types._, klists._
import sys.process._

class CommandGeneration extends FunSuite {

  test("can generate Jellyfish commands") {

    val countExpr = JellyfishExpression(jellyfish.count)(
      jellyfish.count.arguments(
        input(File("reads.fasta"))  ::
        output(File("reads.count")) :: *[AnyDenotation]
      ),
      jellyfish.count.defaults
    )

    val cmdSeq = countExpr.cmd
    println { cmdSeq }
    cmdSeq.!
    assert(

      12 === 12
    )
  }
}
