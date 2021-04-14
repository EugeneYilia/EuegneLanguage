package compiler.compilerFront.lexer

import compiler.compilerFront.common.SyntacticSymbol.{ASSIGN, COMMA, ELIF_KEYWORD, ELSE_KEYWORD, EQUAL, FOR_KEYWORD, FUNCTION_KEYWORD, GE, GT, ID, IF_KEYWORD, INT_CONSTANT, INT_KEYWORD, LE, LEFT_BRACE, LEFT_PAREN, LEFT_SQUARE_BRACKET, LT, MINUS, MULTI, PLUS, PRINTLN, RIGHT_BRACE, RIGHT_PAREN, RIGHT_SQUARE_BRACKET, SEMICOLON}
import compiler.compilerFront.common.Token

object Matcher {
  type MatchResult = Option[(Token, List[Char])]

  def matchKeyword(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case 'i' :: 'n' :: 't' :: restCharList => Some(((INT_KEYWORD, null), restCharList))
    case '函' :: '数' :: restCharList => Some(((FUNCTION_KEYWORD, null), restCharList))
    case '如' :: '果' :: restCharList => Some(((IF_KEYWORD, null), restCharList))
    case 'e' :: 'l' :: 'i' :: 'f' :: restCharList => Some(((ELIF_KEYWORD, null), restCharList))
    case '否' :: '则' :: restCharList => Some(((ELSE_KEYWORD, null), restCharList))
    case 'f' :: 'o' :: 'r' :: restCharList => Some(((FOR_KEYWORD, null), restCharList))
    case _ => None
  }

  def matchBuiltInFunction(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case '打' :: '印' :: restCharList => Some(((PRINTLN, null), restCharList))
    case _ => None
  }

  def matchOperator(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case '+' :: restCharList => Some(((PLUS, null), restCharList))
    case '-' :: restCharList => Some(((MINUS, null), restCharList))
    case '*' :: restCharList => Some(((MULTI, null), restCharList))
    case '=' :: '=' :: restCharList => Some(((EQUAL, null), restCharList))
    case '=' :: restCharList => Some(((ASSIGN, null), restCharList))
    case '>' :: '=' :: restCharList => Some(((GE, null), restCharList))
    case '<' :: '=' :: restCharList => Some(((LE, null), restCharList))
    case '<' :: restCharList => Some(((LT, null), restCharList))
    case '>' :: restCharList => Some(((GT, null), restCharList))
    case _ => None
  }

  def matchSeparator(sourceCharList: List[Char]): MatchResult = sourceCharList match {
    case '(' :: restCharList => Some(((LEFT_PAREN, null), restCharList))
    case ')' :: restCharList => Some(((RIGHT_PAREN, null), restCharList))
    case '[' :: restCharList => Some(((LEFT_SQUARE_BRACKET, null), restCharList))
    case ']' :: restCharList => Some(((RIGHT_SQUARE_BRACKET, null), restCharList))
    case '{' :: restCharList => Some(((LEFT_BRACE, null), restCharList))
    case '}' :: restCharList => Some(((RIGHT_BRACE, null), restCharList))
    case ',' :: restCharList => Some(((COMMA, null), restCharList))
    case ';' :: restCharList => Some(((SEMICOLON, null), restCharList))
    case _ => None
  }

  def matchConstant(sourceCharList: List[Char]): MatchResult = {
    //TODO: 添加Float常量的匹配
    matchIntegerConstant(sourceCharList)
    //      .orElse()
  }

  def matchIntegerConstant(sourceCharList: List[Char]): MatchResult = {
    val (integerCharList, restCharList) = sourceCharList.span(_.isDigit)
    integerCharList match {
      case Nil => None
      case _ => Some(((INT_CONSTANT, integerCharList.mkString), restCharList))
    }
  }

  def matchIdentifier(sourceCharList: List[Char]): MatchResult = {
    sourceCharList match {
      case first :: restCharList =>
        if (matchIdHead(first)) {
          val (matchPart, leftCharList) = restCharList.span(matchIdContent)
          matchPart match {
            case Nil => Some((ID, first.toString), leftCharList)
            case _ => Some((ID, (first +: matchPart).mkString), leftCharList)
          }
        } else {
          None
        }
      case _ => None
    }
  }

  private def matchIdHead(sourceChar: Char): Boolean = {
    (sourceChar >= 'A' && sourceChar <= 'Z') || (sourceChar >= 'a' && sourceChar <= 'z') || sourceChar == '_' || matchChinese(sourceChar)
  }

  private def matchChinese(sourceChar: Char): Boolean = {
    sourceChar >= 0x4E00 && sourceChar <= 0x29FA5
  }

  private def matchIdContent(sourceChar: Char): Boolean = {
    (sourceChar >= 'A' && sourceChar <= 'Z') || (sourceChar >= 'a' && sourceChar <= 'z') || (sourceChar >= '0' && sourceChar <= '9') || sourceChar == '_' || matchChinese(sourceChar)
  }


  def apply(sourceCharList: List[Char]): MatchResult = {
    matchBuiltInFunction(sourceCharList)
      .orElse(matchKeyword(sourceCharList))
      .orElse(matchOperator(sourceCharList))
      .orElse(matchSeparator(sourceCharList))
      .orElse(matchConstant(sourceCharList))
      .orElse(matchIdentifier(sourceCharList))
  }
}
