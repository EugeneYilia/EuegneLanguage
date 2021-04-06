package parser

import common.{Closure, Grammar, Item, SyntacticSymbol}
import common.SyntacticSymbol._
import core.LR
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.MatchResult
import utils.ClosureUtil
import utils.ClosureUtil.MatchResult

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

  test("computeClosure (STARTER,Vector(), Vector(FUNCTIONS),$)") {
    val testItem = (STARTER, Vector(), Vector(FUNCTIONS), $)
    LR.computeClosure(testItem).foreach(println)
  }

  test("computeClosure") {
    Grammar.derivationList.foreach { derivation =>
      println(derivation)
      val result = LR.computeClosure((derivation._1, Vector(), derivation._2, $))
      println(result)

      println()
    }
  }


  val resolve: Item => Option[ClosureUtil.MatchResult] = {
    // 当Derivation不止有一个元素的时候会进入到第一个Case
    case (starter, intermediate, derivationFirst +: derivationLeft, terminalSymbol) if (isNonTerminalSymbol(derivationFirst) && isTerminalSymbol(terminalSymbol)) =>
      Some((starter, intermediate, derivationFirst, derivationLeft, terminalSymbol)): Some[ClosureUtil.MatchResult]
    case _ => None
  }

  test("Resolve") {
    assert(resolve((STARTER, Vector(), Vector(S), $)) === Some((STARTER, Vector(), S, Vector(), $)))
    assert(resolve((S, Vector(), Vector(C, C), $)) === Some((S, Vector(), C, Vector(C), $)))
    assert(resolve((C, Vector(), Vector(c, C), c)).isEmpty)
    assert(resolve((C, Vector(), Vector(d), c)).isEmpty)
  }


  //  test("compute_Table") {
  //    val productionVector = Vector(
  //      Starter -> Vector(S),
  //      S -> Vector(C, C),
  //      C -> Vector(c, C),
  //      C -> Vector(d)
  //    )
  //    val (Table(action, goto),state) = compute_Table(productionVector)
  //    val expected_action = Map(
  //      ((6, d), Action.S(3)),
  //      ((5, c), Action.S(2)),
  //      ((3, c), Action.r(3)),
  //      ((2, c), Action.S(2)),
  //      ((0, d), Action.S(3)),
  //      ((8, $), Action.r(2)),
  //      ((6, c), Action.S(6)),
  //      ((4, c), Action.r(2)),
  //      ((7, $), Action.r(1)),
  //      ((0, c), Action.S(6)),
  //      ((2, d), Action.S(1)),
  //      ((9, $), Action.acc()),
  //      ((4, d), Action.r(2)),
  //      ((1, $), Action.r(3)),
  //      ((3, d), Action.r(3)),
  //      ((5, d), Action.S(1))
  //    )
  //    assertResult(expected_action)(action)
  //    val expected_goto = Map(
  //      ((6, C), 4),
  //      ((0, C), 5),
  //      ((5, C), 7),
  //      ((2, C), 8),
  //      ((0, S), 9)
  //    )
  //    assertResult(expected_goto)(goto)
  //  }
}
