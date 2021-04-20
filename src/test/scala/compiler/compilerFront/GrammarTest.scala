package compiler.compilerFront

import compiler.compilerFront.common.SyntacticSymbol
import org.scalatest.funsuite.AnyFunSuite

class GrammarTest extends AnyFunSuite {
  test("fetch grammar") {

    SyntacticSymbol.values.foreach(println)

//    SyntacticSymbol.
  }

  test("test symbolMap") {
    SyntacticSymbol.syntacticSymbolMap.foreach(element=>{
      println(element._1)
      println(element._2)
      println()
    })
  }
}
