package lexer

import common.SyntacticSymbol.{ASSIGN, ELIF_KEYWORD, ELSE_KEYWORD, EQUAL, FOR_KEYWORD, FUNCTION_KEYWORD, GE, GT, IF, INT_KEYWORD, LE, LEFT_PAREN, LT, MINUS, MULTI, PLUS, PRINTLN}
import common.Token

object Matcher {
  type MatchResult = Option[(Token, List[Char])]

  def matchKeyword(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case 'i' :: 'n' :: 't' :: restCharList => Some(((INT_KEYWORD, null), restCharList))
    case 'f' :: 'u' :: 'n' :: 'c' :: restCharList => Some(((FUNCTION_KEYWORD, null), restCharList))
    case 'i' :: 'f' :: restCharList => Some(((IF, null), restCharList))
    case 'e' :: 'l' :: 'i' :: 'f' :: restCharList => Some(((ELIF_KEYWORD, null), restCharList))
    case 'e' :: 'l' :: 's' :: 'e' :: restCharList => Some(((ELSE_KEYWORD, null), restCharList))
    case 'f' :: 'o' :: 'r' :: restCharList => Some(((FOR_KEYWORD, null), restCharList))
    case _ => None
  }

  def matchBuiltInFunction(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case 'p' :: 'r' :: 'i' :: 'n' :: 't' :: 'l' :: 'n' :: restCharList => Some(((PRINTLN, null), restCharList))
    case _ => None
  }

  def matchOperator(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case '+' :: restCharList => Some(((PLUS, null), restCharList))
    case '-' :: restCharList => Some(((MINUS, null), restCharList))
    case '*' :: restCharList => Some(((MULTI, null), restCharList))
    case '=' :: restCharList => Some(((ASSIGN, null), restCharList))
    case '>' :: '=' :: restCharList => Some(((GE, null), restCharList))
    case '<' :: '+' :: restCharList => Some(((LE, null), restCharList))
    case '<' :: restCharList => Some(((LT, null), restCharList))
    case '>' :: restCharList => Some(((GT, null), restCharList))
    case '=' :: '=' :: restCharList => Some(((EQUAL,null),restCharList))
    case _ => None
  }
  def matchSeparator(sourceCharList:List[Char]):MatchResult = sourceCharList match {
    case '(' :: restCharList => Some(((LEFT_PAREN,null),restCharList))
    case ')'
  }

  def apply(sourceCharList: List[Char]): MatchResult = {
    matchKeyword(sourceCharList)
      .orElse()
      .orElse()
      .orElse()
      .orElse(matchBuiltInFunction(sourceCharList))
  }


}
