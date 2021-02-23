package lexer

import org.scalatest.funsuite.AnyFunSuite

class LexerTest extends AnyFunSuite {

  test("skipWhiteSpace()") {
    //    assert("XXXXXX" == "YYYYYYYYYY")
    val source = List('X', 'A', '\n', 'S', 'Q')
    println(source.span(_.isWhitespace))
    println(source.span(_.isWhitespace)._1)
    println(source.span(_.isWhitespace)._2)
    println(Lexer.skipWhiteSpace(source))
  }
}
