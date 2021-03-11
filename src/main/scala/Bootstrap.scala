import common.{Grammar, Item, SyntacticSymbol}
import core.LR
import lexer.Lexer
import parser.eugeneParser.EugeneParser

import scala.io.Source

object Bootstrap extends App{
  println(s"filePath: ")
  println(args(0))
  println()

  val file = Source.fromFile(args(0))
  val fileContent = file.mkString
  println(s"fileContent: ")
  println(fileContent)
  println()

  var tokens = Lexer(fileContent) :+ (SyntacticSymbol.$,null)

  println(s"tokens: ")
  println(tokens.mkString)
  println()

  println("Grammars: ")
  Grammar.derivationList.foreach(println)
  println()

  println("First: ")
  Grammar.first.foreach(println)
  println()

  println("Original Closure: ")
  val originalItem : Item = (SyntacticSymbol.STARTER, Vector(), Vector(SyntacticSymbol.FUNCTIONS), SyntacticSymbol.$)
  LR.computeClosure(originalItem).foreach(println)
  println()

  println("Parse: ")
  LR.computeAnalysisTable(Grammar.derivationList)
  EugeneParser(tokens)
}
