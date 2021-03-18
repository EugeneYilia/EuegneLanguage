package utils

import common.SyntacticSymbol.SyntacticSymbol
import common.{Closure, Item}
import core.LR

object GotoUtil {
  def goto(closure: Closure, specificSyntacticSymbol: SyntacticSymbol): Closure = {
    def resolve(item: Item): Option[Item] = item match {
      case (starter, usedVector, derivationFirst +: derivationLeft, terminal) if derivationFirst == specificSyntacticSymbol
      => Some((starter, usedVector :+ derivationFirst, derivationLeft, terminal))
      case _ => None
    }

    val tempResult = closure
      .flatMap(resolve) // 由移位后的Item构成的ItemSet
    val finalResult =
      tempResult
        .flatMap(LR.computeClosure) // 由其中的每一项构成的closure形成的Set[Closure]，进行flatten后构成Set[Item] 即为最后的Closure结果
    finalResult
  }

}

//HashSet(
//(FUNCTION,Vector(),Vector(FUNCTION_KEYWORD, ID, LEFT_PAREN, RIGHT_PAREN, BLOCK),$),
//(FUNCTION,Vector(),Vector(FUNCTION_KEYWORD, ID, LEFT_PAREN, RIGHT_PAREN, BLOCK),FUNCTION_KEYWORD),
//(FUNCTIONS,Vector(FUNCTIONS),Vector(FUNCTION),FUNCTION_KEYWORD),
//(STARTER,Vector(FUNCTIONS),Vector(),$),
//(FUNCTIONS,Vector(FUNCTIONS),Vector(FUNCTION),$)
//)
