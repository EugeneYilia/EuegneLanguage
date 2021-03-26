package parser.lrParser

import common.ASTNode.Node
import common.ASTNode.nonTerminalNode.{BasicNode, FunctionNode, FunctionsNode}
import common._
import parser.lrParser.LRParser.reduce

import scala.annotation.tailrec

final class LRParser(val analysisTable: AnalysisTable) {

  private val actionMap = analysisTable._1
  private val gotoMap = analysisTable._2

  @tailrec
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
            val currentState = newStateStack.head

            // goto state 和 之前 state 到底 闭包都是什么
            val gotoState = gotoMap((currentState, syntacticSymbolStarter))

            // for dev *
            val indexClosureMap = Grammar.indexClosureMap
            println(indexClosureMap(currentState))
            println(indexClosureMap(gotoState))
            // for dev **


            /**
             * usedNodeVector： 要进行reduce的node构成的vector
             * leftNodeVector： 剩余的node构成的vector
             */
            val (usedNodeVector, leftNodeVector) = nodeStack.splitAt(productionLength)
            val reducedNode = reduce(derivation, usedNodeVector)

            parse(gotoState +: newStateStack, reducedNode +: leftNodeVector, tokensLeft)
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

  // 注意操作的对象是Node Stack，因此后来的元素在顶上，先来的元素在栈底   2  1  0
  def reduce(derivation: Derivation, usedNodeVector: Vector[Node]): Node = derivation match {
    case (SyntacticSymbol.FUNCTIONS, Vector(SyntacticSymbol.FUNCTION, SyntacticSymbol.FUNCTIONS)) =>
      FunctionsNode(usedNodeVector(0).asInstanceOf[FunctionsNode].functionList :+ usedNodeVector(1).asInstanceOf[FunctionNode])
    case (SyntacticSymbol.FUNCTIONS, Vector(SyntacticSymbol.FUNCTION)) =>
      FunctionsNode(List(usedNodeVector.head.asInstanceOf[FunctionNode]))

    case (SyntacticSymbol.FUNCTION, Vector(SyntacticSymbol.FUNCTION_KEYWORD))


    case _ =>
      System.err.println("this derivation not exist in Grammar.")
      throw new RuntimeException("this derivation not exist in Grammar.")
  }
}
