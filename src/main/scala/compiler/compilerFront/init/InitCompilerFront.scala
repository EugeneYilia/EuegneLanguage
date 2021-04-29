package compiler.compilerFront.init

import compiler.compilerFront.common.{Grammar, Item, SyntacticSymbol}
import compiler.compilerFront.core.LR
import compiler.compilerFront.parser.lrParser.LRParser

object InitCompilerFront {
  def initCompilerFront(): Unit = {
    println("Grammars: ")
    Grammar.getUsedDerivationList.foreach(println)
    println()

    println("First: ")
    Grammar.first().foreach(println)
    println()

    println("Closure Vector: ")
    Grammar.closureVector.foreach(element => {
      println("Closure: ")
      element.foreach(item => {
        println(s"Item: ${item}")
      })
      println()
    })

    val initDerivation = Grammar.derivationList.filter(_._1 == SyntacticSymbol.STARTER).map(_._2).head
    println("Original Closure: ")
    val originalItem: Item = (SyntacticSymbol.STARTER, Vector(),initDerivation, SyntacticSymbol.$)
    LR.computeClosure(originalItem).foreach(println)
    println()

    println("Analysis Table: ")
    val analysisTable = LR.computeAnalysisTable()
    val actionMap = analysisTable._1
    val gotoMap = analysisTable._2
    println("Action Map")
    actionMap.foreach(println)
    println()
    println("Goto Map")
    gotoMap.foreach(println)
    println()

    val lrParser = LRParser(analysisTable)
    // for debug **
    lrParser.showAnalysisTable()
    // for debug ****
  }
}
