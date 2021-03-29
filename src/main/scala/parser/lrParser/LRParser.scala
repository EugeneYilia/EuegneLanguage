package parser.lrParser

import common.ASTNode.{ExpressionNode, Node}
import common.ASTNode.nonTerminalNode._
import common.ASTNode.terminalNode.{AssignNode, BinaryOpNode, FunctionCallNode, IDNode, PrintlnNode}
import common.ASTNode.terminalNode.literalNode.IntegerLiteralNode
import common.SyntacticSymbol._
import common._
import parser.lrParser.LRParser.reduce

import scala.annotation.tailrec
import scala.collection.mutable

final class LRParser(val analysisTable: AnalysisTable) {

  private val actionMap = analysisTable._1
  private val gotoMap = analysisTable._2

  // For Dev
  private lazy val sortActionMap: mutable.TreeMap[(State, SyntacticSymbol), Action] = {
    mutable.TreeMap(actionMap.toList: _*)
  }

  @tailrec
  def parse(stateStack: Vector[State], nodeStack: Vector[Node], tokensLeft: Vector[Token]): ParseResult = {
    val currentToken = tokensLeft.head
    val currentTokenSymbol = currentToken._1

    val currentState = stateStack.head
    sortActionMap.get((currentState, currentTokenSymbol)) match {
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
            println("Current Closure: ")
            println(indexClosureMap(currentState))
            println("Goto Closure: ")
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
        val currentElement = (currentState, currentTokenSymbol, currentToken._2)
        System.err.println(currentElement)
        throw new RuntimeException(s"$currentElement parse error")
    }
  }
}

object LRParser {
  def apply(analysisTable: AnalysisTable): LRParser = new LRParser(analysisTable)

  // 注意操作的对象是Node Stack，因此后来的元素在顶上，先来的元素在栈底   2  1  0
  def reduce(derivation: Derivation, usedNodeVector: Vector[Node]): Node = derivation match {
    case FUNCTIONS -> Vector(FUNCTION, FUNCTIONS) =>
      FunctionsNode(usedNodeVector(1).asInstanceOf[FunctionNode] +: usedNodeVector(0).asInstanceOf[FunctionsNode].functionList)
    case FUNCTIONS -> Vector(FUNCTION) =>
      FunctionsNode(List(usedNodeVector.head.asInstanceOf[FunctionNode]))

    case FUNCTION -> Vector(FUNCTION_KEYWORD, ID, LEFT_PAREN, RIGHT_PAREN, BLOCK) =>
      FunctionNode(usedNodeVector(3).asInstanceOf[BasicNode].value, usedNodeVector(0).asInstanceOf[StatementsNode])

    case BLOCK -> Vector(LEFT_BRACE, STATEMENTS, RIGHT_BRACE) =>
      usedNodeVector(1).asInstanceOf[StatementsNode]

    case STATEMENTS -> Vector(STATEMENT, STATEMENTS) =>
      StatementsNode(usedNodeVector(1).asInstanceOf[StatementNode] +: usedNodeVector(0).asInstanceOf[StatementsNode].statementList)
    case STATEMENTS -> Vector(STATEMENT) =>
      StatementsNode(List(usedNodeVector.head.asInstanceOf[StatementNode]))

    case STATEMENT -> Vector(EXPRESSION, SEMICOLON) =>
      StatementNode(usedNodeVector(1).asInstanceOf[ExpressionNode])

    case EXPRESSION -> Vector(INT_CONSTANT) =>
      IntegerLiteralNode(usedNodeVector.head.asInstanceOf[BasicNode].value.toInt)
    case EXPRESSION -> Vector(PRINTLN, LEFT_PAREN, EXPRESSION, RIGHT_PAREN) =>
      PrintlnNode(usedNodeVector(1).asInstanceOf[ExpressionNode])
    case EXPRESSION -> Vector(ID) =>
      IDNode(usedNodeVector(0).asInstanceOf[BasicNode].value)
    case EXPRESSION -> Vector(ID, LEFT_PAREN, RIGHT_PAREN) =>
      FunctionCallNode(usedNodeVector(2).asInstanceOf[BasicNode].value)
    case EXPRESSION -> Vector(EXPRESSION, PLUS, EXPRESSION) =>
      BinaryOpNode('+', usedNodeVector(2).asInstanceOf[ExpressionNode], usedNodeVector(0).asInstanceOf[ExpressionNode])
    case EXPRESSION -> Vector(EXPRESSION, MINUS, EXPRESSION) =>
      BinaryOpNode('-', usedNodeVector(2).asInstanceOf[ExpressionNode], usedNodeVector(0).asInstanceOf[ExpressionNode])
    case EXPRESSION -> Vector(EXPRESSION, MULTI, EXPRESSION) =>
      BinaryOpNode('*', usedNodeVector(2).asInstanceOf[ExpressionNode], usedNodeVector(0).asInstanceOf[ExpressionNode])
    case EXPRESSION -> Vector(ID, ASSIGN, EXPRESSION) =>
      AssignNode(usedNodeVector(2).asInstanceOf[BasicNode].value, usedNodeVector(0).asInstanceOf[ExpressionNode])
    case EXPRESSION -> Vector(IF_KEYWORD, LEFT_PAREN, EXPRESSION, RIGHT_PAREN, BLOCK, ELSE_KEYWORD, BLOCK) =>
      IfNode(usedNodeVector(4).asInstanceOf[ExpressionNode], usedNodeVector(2).asInstanceOf[StatementsNode], usedNodeVector(0).asInstanceOf[StatementsNode])

    case _ =>
      val errorMsg = s"this derivation: $derivation and usedNodeVector: $usedNodeVector not exist in Grammar."
      System.err.println(errorMsg)
      throw new RuntimeException(errorMsg)
  }
}
