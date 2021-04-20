package compiler.compilerFront

import compiler.compilerFront.common.SyntacticSymbol
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable

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

  test("test vector"){
//    var x = mutable.List[String]()
//    x+= "sadasd"
//    println(x)
  }
}
