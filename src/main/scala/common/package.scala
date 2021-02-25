import common.SyntacticSymbol.SyntacticSymbol

package object common {
  object SyntacticSymbol extends Enumeration {
    type SyntacticSymbol = Value

    val STARTER,$ = Value
    val FUNCTIONS,FUNCTION,STATEMENTS,STATEMENT,EXPRESSION,BLOCK = Value
    val INT_KEYWORD,FUNCTION_KEYWORD,IF_KEYWORD,ELIF_KEYWORD,ELSE_KEYWORD,FOR_KEYWORD = Value
    val PLUS,ASSIGN,MINUS,MULTI,EQUAL,LE,GE,LT,GT = Value
    val PRINTLN : SyntacticSymbol = Value
    //      ;       ,
    val SEMICOLON,COMMA = Value
    val ID,INT_CONSTANT,FLOAT_CONSTANT = Value
    //      （          ）               [                  ]               {            }
    val LEFT_PAREN,RIGHT_PAREN,LEFT_SQUARE_BRACKET,RIGHT_SQUARE_BRACKET,LEFT_BRACE,RIGHT_BRACE = Value

    val terminalSymbolSet = Set($,INT_KEYWORD,FUNCTION_KEYWORD,PLUS,ASSIGN,MINUS,MULTI,LE,GE,LT,GT,EQUAL,SEMICOLON,
      COMMA,ID,LEFT_PAREN,RIGHT_PAREN,LEFT_SQUARE_BRACKET,RIGHT_SQUARE_BRACKET,LEFT_BRACE,RIGHT_BRACE)
    val nonTerminalSymbolSet = Set(STARTER,FUNCTIONS,FUNCTION,STATEMENTS,STATEMENT,EXPRESSION,BLOCK)

    def isTerminalSymbol(syntacticSymbol: SyntacticSymbol) : Boolean = terminalSymbolSet.contains(syntacticSymbol)
    def isNonTerminalSymbol(syntacticSymbol: SyntacticSymbol) : Boolean = nonTerminalSymbolSet.contains(syntacticSymbol)
  }

  type Token = (SyntacticSymbol,String)


}
