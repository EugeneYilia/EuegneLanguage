package object common {
  object SyntacticSymbol extends Enumeration {
    type SyntacticSymbol = Value

    val STARTER,$ = Value
    val FUNCTIONS,FUNCTION,STATEMENTS,STATEMENT,EXPRESSION,BLOCK = Value
    val INT_KEYWORD,FUNCTION_KEYWORD = Value
    val PLUS,ASSIGN,MINUS,MULTI,LE,GE,LT,GT,EQ = Value
    //      ;       ,
    val SEMICOLON,COMMA = Value
    val ID = Value
    //      （          ）               [                  ]               {            }
    val LEFT_PAREN,RIGHT_PAREN,LEFT_SQUARE_BRACKET,RIGHT_SQUARE_BRACKET,LEFT_BRACE,RIGHT_BRACE = Value

  }
}
