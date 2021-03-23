package parser.lrParser

import common.ASTNode.Node
import common.ASTNode.nonTerminalNode.BasicNode
import common.{AnalysisTable, ParseResult, State, Token}

class LRParser(val analysisTable: AnalysisTable) {

  private val actionMap = analysisTable._1
  private val gotoMap = analysisTable._2

  def parse(stateStack: Vector[State], nodeStack: Vector[Node], tokensLeft: Vector[Token]): ParseResult = {
    val currentToken = tokensLeft.head
    val currentTokenSymbol = currentToken._1

    val currentState = stateStack.head
    actionMap.get((currentState, currentTokenSymbol)) match {
      case Some(action) =>
        action match {
          case Shift(newState) => {

          }
          case Reduce(derivation) => {

          }
          case Accept() =>
            println("Parse Accept!")
            (nodeStack.head, tokensLeft)
        }
      case None =>
        val currentElement = (currentState, currentTokenSymbol)
        System.err.println(currentElement)
        throw new RuntimeException(s"${(currentState, currentTokenSymbol)} parse error")
    }
  }
}

object LRParser {
  def apply(analysisTable: AnalysisTable): LRParser = new LRParser(analysisTable)

}
