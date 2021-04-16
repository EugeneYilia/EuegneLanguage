package compiler.compilerFront.utils

import compiler.compilerFront.common.DerivationList

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

  def transformGrammar(crudeGrammar:String):DerivationList = {

  }
}
