package compiler.compilerFront.lexer

import compiler.compilerFront.common.SyntacticSymbol._
import org.scalatest.funsuite.AnyFunSuite

class LexerTest extends AnyFunSuite {

  test("skipWhiteSpace") {
    //    assert("XXXXXX" == "YYYYYYYYYY")
    val source = List('\n','X', 'A', '\n', 'S', 'Q')
    println(source.span(_.isWhitespace))
    println(source.span(_.isWhitespace)._1)
    println(source.span(_.isWhitespace)._2)
    println(Lexer.skipWhiteSpace(source))
  }

  test("skip blank") {
    val result = Lexer.skipWhiteSpace(" \t\n\r12\t4;\n".toList)
    assert(result == "12\t4;\n".toList)
  }

  test("apply") {
    val code: String = "if 12>5\n6\t \nelse 10;"
    val result = Lexer(code)
    val expected = List((IF_KEYWORD, null), (INT_CONSTANT, "12"), (GT, null), (INT_CONSTANT, "5"), (INT_CONSTANT, "6"), (ELSE_KEYWORD, null), (INT_CONSTANT, "10"), (SEMICOLON, null))
    assert(result == expected)
  }


}
