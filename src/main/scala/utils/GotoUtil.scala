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

    closure
      .flatMap(resolve)// 由移位后的Item构成的ItemSet
      .flatMap(LR.computeClosure)// 由其中的每一项构成的closure形成的Set[Closure]，进行flatten后构成Set[Item] 即为最后的Closure结果
  }

}
