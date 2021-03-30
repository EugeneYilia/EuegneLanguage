package lexer

import common.SyntacticSymbol._
import org.scalatest.funsuite.AnyFunSuite

class MatcherTest extends AnyFunSuite{
  test("keyword_matcher with rest char") {
    val (token, rest) = Matcher.matchKeyword("if(a==b)".toList).get
    assert(token == (IF_KEYWORD, null))
    assert(rest == "(a==b)".toList)
  }

  test("keyword_matcher without rest char") {
    val (token, rest) = Matcher.matchKeyword("if".toList).get
    assert(token == (IF_KEYWORD, null))
    assert(rest == Nil)
  }

  test("operator_matcher with rest char") {
    val (token, rest) = Matcher.matchOperator(">12".toList).get
    assert(token == (GT, null))
    assert(rest == "12".toList)
  }

  test("integer_constant_matcher with rest char") {
    val (token, rest) = Matcher.matchConstant("123456;if(true)".toList).get
    assert(token == (INT_CONSTANT, "123456"))
    assert(rest == ";if(true)".toList)
  }

  test("integer_constant_matcher without rest char") {
    val (token, rest) = Matcher.matchConstant("123456".toList).get
    assert(token == (INT_CONSTANT, "123456"))
    assert(rest == Nil)
  }

  test("apply keyword") {
    val (token, rest) = Matcher("if(6>7)".toList).get
    assert(token == (IF_KEYWORD, null))
    assert(rest == "(6>7)".toList)
  }

  test("apply operator") {
    val (token, rest) = Matcher(">6".toList).get
    assert(token == (GT, null))
    assert(rest == "6".toList)
  }

  test("apply constant") {
    val (token, rest) = Matcher("123456;1+1".toList).get
    assert(token == (INT_CONSTANT, "123456"))
    assert(rest == ";1+1".toList)
  }

  test("apply identifier") {
    val (token, rest) = Matcher("abc 12>3".toList).get
    assert(token == (ID, "abc"))
    assert(rest == " 12>3".toList)
  }
}
