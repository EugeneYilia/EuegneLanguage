package common

import common.SyntacticSymbol.{BLOCK, FUNCTION, FUNCTIONS, FUNCTION_KEYWORD, ID, LEFT_PAREN, RIGHT_PAREN, STARTER}

object Grammar {
  val derivationList :List[Derivation] = List[Derivation](
    STARTER -> Vector(FUNCTIONS),
    FUNCTIONS -> Vector(FUNCTIONS,FUNCTION),
    FUNCTIONS -> Vector(FUNCTION),
    FUNCTION -> Vector(FUNCTION_KEYWORD,ID,LEFT_PAREN,RIGHT_PAREN,BLOCK)


  )
}
