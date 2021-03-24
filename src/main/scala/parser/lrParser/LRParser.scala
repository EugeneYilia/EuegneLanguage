package parser.lrParser

import common.ASTNode.Node
import common.ASTNode.nonTerminalNode.BasicNode
import common.SyntacticSymbol.SyntacticSymbol
import common.{AnalysisTable, Derivation, ParseResult, State, Token}
import parser.lrParser.LRParser.reduce

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
          case Shift(newState) =>
            val currentTokenValue = currentToken._2
            val newNode = BasicNode(currentTokenValue)
            parse(newState +: stateStack, newNode +: nodeStack, tokensLeft.tail)
          case Reduce(derivation) =>
            val syntacticSymbolStarter = derivation._1
            val production = derivation._2
            val productionLength = production.length
            val newStateStack = stateStack.drop(productionLength)

            /**
             * usedNodeVector： 要进行reduce的node构成的vector
             * leftNodeVector： 剩余的node构成的vector
             */
            val (usedNodeVector, leftNodeVector) = nodeStack.splitAt(productionLength)
            val reducedNode = reduce(derivation, usedNodeVector)

            (BasicNode(""), Vector())
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

  def reduce(derivation:Derivation,usedNodeVector:Vector[Node]) :Node = derivation match {
    case
  }
}
