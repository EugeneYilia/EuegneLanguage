import common.ASTNode.Node
import common.ASTNode.nonTerminalNode.BasicNode
import common.SyntacticSymbol.SyntacticSymbol
import common.{Grammar, Item, SyntacticSymbol}
import core.LR
import core.LR.computeClosure
import lexer.Lexer

import scala.io.Source

object Bootstrap extends App {

  println(s"filePath: ")
  println(args(0))
  println()

  val file = Source.fromFile(args(0))
  val fileContent = file.mkString
  println(s"fileContent: ")
  println(fileContent)
  println()

  var tokens = Lexer(fileContent) :+ (SyntacticSymbol.$, null)

  println(s"tokens: ")
  println(tokens.mkString)
  println()

  println("Grammars: ")
  Grammar.getUsedDerivationList.foreach(println)
  println()

  println("First: ")
  Grammar.first().foreach(println)
  println()

  println("Original Closure: ")
  val originalItem: Item = (SyntacticSymbol.STARTER, Vector(), Vector(SyntacticSymbol.FUNCTIONS), SyntacticSymbol.$)
  LR.computeClosure(originalItem).foreach(println)
  println()

  println("Parse: ")
  LR.computeAnalysisTable()


  val initDerivation = Grammar.derivationList.filter(_._1 == SyntacticSymbol.STARTER).map(_._2).head
  val initItem = (SyntacticSymbol.STARTER, Vector[SyntacticSymbol](), initDerivation, SyntacticSymbol.$)
  val initClosure = computeClosure(initItem)
  val initState = LR.computeState(initClosure)

  val analysisTable = LR.computeAnalysisTable()
  val actionMap = analysisTable._1
  val gotoMap = analysisTable._2

  val initStateStack = Vector(initState)
  val initNodeStack = Vector[Node](BasicNode(""))

  // 文法 Grammar -> 分析表 AnalysisTable -> 抽象语法树 AST

}
