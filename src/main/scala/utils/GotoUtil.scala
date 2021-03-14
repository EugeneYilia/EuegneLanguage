package utils

import common.SyntacticSymbol.SyntacticSymbol
import common.{Closure, Item, ItemSet}
import core.LR

object GotoUtil {
  def goto(closure: Closure, specificSyntacticSymbol: SyntacticSymbol): Closure = {
    def resolve(item: Item): Option[Item] = item match {
      case (starter, usedVector, derivationFirst +: derivationLeft, terminal) if derivationFirst == specificSyntacticSymbol => Some((starter, usedVector :+ derivationFirst, derivationLeft, terminal))
      case _ => None
    }

    val result = closure.flatMap(resolve).flatMap(LR.computeClosure)
    result
  }

  def goto(closure: Closure): Closure = {
    def resolve(item: Item): Option[Item] = item match {
      case (starter, usedVector, derivationFirst +: derivationLeft, terminal) => Some((starter, usedVector :+ derivationFirst, derivationLeft, terminal))
      case _ => None
    }
//              由移位后的Item构成的ItemSet
    val result = closure.flatMap(resolve).flatMap(LR.computeClosure)
    result
  }


}
