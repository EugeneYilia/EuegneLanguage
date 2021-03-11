package utils

import common.{Closure, Grammar, Item, ItemSet}
import common.SyntacticSymbol.{SyntacticSymbol, isNonTerminalSymbol, isTerminalSymbol}

object ClosureUtil {
  //                                                           NonTerminalSymbol
  //                       STARTER        IntermediateVec       DerivationFirst       DerivationLeft       TerminalSymbol
  type MatchResult = (SyntacticSymbol, Vector[SyntacticSymbol], SyntacticSymbol, Vector[SyntacticSymbol], SyntacticSymbol)

//  def computeOriginalItemSet(): ItemSet = {
//
//  }

  def resolve(item: Item): Option[MatchResult] = item match {
    // 当Derivation不止有一个元素的时候会进入到第一个Case
    case (starter, intermediate, derivationFirst +: derivationLeft, terminalSymbol) if (isNonTerminalSymbol(derivationFirst) && isTerminalSymbol(terminalSymbol)) =>
      Some((starter, intermediate, derivationFirst, derivationLeft, terminalSymbol)): Some[MatchResult]
    case _ => None
  }

  def computeFirstSet(derivationLeft:Vector[SyntacticSymbol]):Option[Set[SyntacticSymbol]] = {
    if(derivationLeft.isEmpty) None
    else Some(Grammar.first(derivationLeft.head))
  }

  // 计算由MatchResult中的derivationFirst非终结符所能产生的所有项目集
  def originalStep(item: Item): Closure = {
    resolve(item) match {
      case Some(matchResult: MatchResult) =>
        val derivationFirst = matchResult._3 // 此为非终结符
        val derivationLeft = matchResult._4
        val terminalSymbol = matchResult._5
        val derivationFirstProductions = Grammar.derivationList.withFilter(_._1 == derivationFirst).map(_._2)
        (for {
          derivationFirstProduction <- derivationFirstProductions
          derivationLeftFirstTerminalSymbol <- computeFirstSet(derivationLeft) match {
            case Some(firstSet) => firstSet
            case None => Set(terminalSymbol)
          }
        } yield (derivationFirst,Vector(),derivationFirstProduction,derivationLeftFirstTerminalSymbol)).toSet
      case None => Set()
    }
  }


}
