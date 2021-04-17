package compiler.compilerFront.utils

import compiler.compilerFront.common.DerivationList
import compiler.compilerFront.common.SyntacticSymbol.SyntacticSymbol

object GrammarUtil {
  def formatGrammar(grammarList: DerivationList): String = {
    val stringBuilder = new StringBuilder
    grammarList.foreach(element => {
      stringBuilder append element._1
      stringBuilder append "  ->  "
      stringBuilder append "["
      for (index <- element._2.indices) {
        stringBuilder append element._2(index)
        if (index != (element._2.size - 1)) {
          stringBuilder append ','
        }
      }
      stringBuilder append "]"
      stringBuilder append '\n'
    })
    stringBuilder.toString
  }

  def transformGrammar(crudeGrammar: String): DerivationList = {
    val derivationList = List()


    val grammarArray = crudeGrammar.split("\n")
    grammarArray.foreach(grammar => {
      val grammarParts = grammar.split("->")
      val nonTerminalSymbol = grammarParts(0).trim
      val production = grammarParts(1).trim.drop(1).dropRight(1)
      val productionSymbol = production.split(",")

      val tempSymbolVector = Vector[SyntacticSymbol]()
      productionSymbol.foreach(crudeSymbol => {
        val symbol = crudeSymbol.trim

      })
    })

    derivationList
  }
}
