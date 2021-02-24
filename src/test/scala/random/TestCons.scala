package random

import org.scalatest.funsuite.AnyFunSuite

class TestCons extends AnyFunSuite {
  test("test::") {
    val source = List('1', '2', '3', '4', '5')
    source match {
      case a :: b :: c => {
        println(a)
        println(b)
        println(c)
      }


    }
    //    println(source.span(_.isWhitespace))
    //    println(source.span(_.isWhitespace)._1)
    //    println(source.span(_.isWhitespace)._2)
    //    println(Lexer.skipWhiteSpace(source))
  }
}
