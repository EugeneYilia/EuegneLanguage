package parser

import common.{Closure, Grammar, SyntacticSymbol}
import common.SyntacticSymbol._
import core.LR
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable
import scala.collection.immutable.HashSet

class parserTest extends AnyFunSuite {
  test("compute first") {
    val production__set = List(
      E -> Vector(T),
      T -> Vector(F),
      T -> Vector(a),
      F -> Vector(LEFT_PAREN, E, RIGHT_PAREN),
      F -> Vector(b)
    )
    assert(LR.computeFirst(production__set).filter(p => SyntacticSymbol.isNonTerminalSymbol(p._1)) == Map(T -> Set(b, LEFT_PAREN, a), E -> Set(b, LEFT_PAREN, a), F -> Set(b, LEFT_PAREN)))
  }

  test("computeAllItems") {
    val productionSet = List(
      STARTER -> Vector(EXPRESSION),
      EXPRESSION -> Vector(C, C),
      C -> Vector(c, C),
      C -> Vector(d)
    )
    Grammar.setUsedDerivationList(productionSet)
    val result = HashSet((LR.computeItems().toVector): _*)

    val expected: immutable.Set[Closure] = immutable.Set(
      HashSet((C, Vector(), Vector(d), d), (C, Vector(), Vector(d), c), (EXPRESSION, Vector(), Vector(C, C), $), (C, Vector(), Vector(c, C), d), (C, Vector(), Vector(c, C), c), (STARTER, Vector(), Vector(EXPRESSION), $)), // 0
      HashSet((STARTER, Vector(EXPRESSION), Vector(), $)), // 1
      HashSet((EXPRESSION, Vector(C), Vector(C), $), (C, Vector(), Vector(c, C), $), (C, Vector(), Vector(d), $)), // 2
      HashSet((C, Vector(), Vector(d), d), (C, Vector(), Vector(d), c), (C, Vector(), Vector(c, C), d), (C, Vector(), Vector(c, C), c), (C, Vector(c), Vector(C), c), (C, Vector(c), Vector(C), d)), // 3
      HashSet((C, Vector(d), Vector(), d), (C, Vector(d), Vector(), c)), // 4
      HashSet((EXPRESSION, Vector(C, C), Vector(), $)), // 5
      HashSet((C, Vector(c), Vector(C), $), (C, Vector(), Vector(c, C), $), (C, Vector(), Vector(d), $)), // 6
      HashSet((C, Vector(d), Vector(), $)), // 7
      HashSet((C, Vector(c, C), Vector(), d), (C, Vector(c, C), Vector(), c)), // 8
      HashSet((C, Vector(c, C), Vector(), $)) // 9
    )

    println("result: ")
    result.foreach(println)
    println
    println("expected: ")
    expected.foreach(println)
    println
    //    println(result.diff(expected))
    //    println(expected.diff(result))
    assertResult(expected)(result)
  }

  test("computeClosure (STARTER,Vector(FUNCTIONS), Vector(),$)") {
    val testItem = (STARTER, Vector(FUNCTIONS), Vector(), $)
    println(LR.computeClosure(testItem))
  }
}
