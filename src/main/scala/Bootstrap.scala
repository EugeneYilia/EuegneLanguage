import common.{Grammar, SyntacticSymbol}
import core.LR
import lexer.Lexer
import parser.eugeneParser.EugeneParser

import scala.io.Source

object Bootstrap extends App{
  println(s"filePath: ${args(0)}")
  val file = Source.fromFile(args(0))
  val fileContent = file.mkString
  println(s"fileContent: $fileContent")
  var tokens = Lexer(fileContent) :+ (SyntacticSymbol.$,null)
  println(s"tokens: ${tokens.mkString}")
  println("Grammars: ")
  Grammar.derivationList.foreach(println)

  LR.computeAnalysisTable(Grammar.derivationList)
  EugeneParser(tokens)
}
