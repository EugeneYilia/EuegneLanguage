package compiler.compilerFront.utils

import compiler.compilerFront.common.Grammar
import compiler.compilerFront.common.SyntacticSymbol._
import compiler.compilerFront.core.LR
import org.scalatest.funsuite.AnyFunSuite

class closureUtilTest extends AnyFunSuite {
  val productionList = List(
    STARTER -> Vector(S),
    S -> Vector(C, C),
    C -> Vector(c, C),
    C -> Vector(d)
  )
  Grammar.setUsedDerivationList(productionList)

  test("Single Step") {

    val item = (STARTER, Vector(), Vector(S), $)
    val result = ClosureUtil.singleStep(item)
    assert(result == Set((S, Vector(), Vector(C, C), $)))
  }

  test("Closure") {
    val item = (STARTER, Vector(), Vector(S), $)
    val result = LR.computeClosure(item)
    val expected = Set(
      (STARTER, Vector(), Vector(S), $),
      (S, Vector(), Vector(C, C), $),
      (C, Vector(), Vector(d), d),
      (C, Vector(), Vector(d), c),
      (C, Vector(), Vector(c, C), d),
      (C, Vector(), Vector(c, C), c)
    )
    assert(result == expected)
  }
}
