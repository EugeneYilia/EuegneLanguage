import lexer.Lexer

import scala.io.Source

object Bootstrap extends App{
  println(s"filePath: ${args(0)}")
  val file = Source.fromFile(args(0))
  val fileContent = file.mkString
  println(s"fileContent: $fileContent")
  val tokens = Lexer(fileContent)
  println(s"tokens: ${tokens.mkString}")

}
