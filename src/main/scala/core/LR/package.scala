package core

import common.SyntacticSymbol.SyntacticSymbol
import common.{DerivationList, First, Grammar, SyntacticSymbol}

import scala.collection.mutable

package object LR {
  def computeAnalysisTable(derivationList: DerivationList): Unit = {
    //    val derivationMap = derivationList.zipWithIndex.toMap

  }

  def computeFirst(derivationList: DerivationList): First = {
    // 终结符号的First集合只由其自身构成
    val initTerminalFirstMap: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = computeTerminalSymbolFirst

    // 添加非终结符号的First集合至FirstMap
    val initNonTerminalFirstMap: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = computeNonTerminalSymbolFirst()

    computeAllSymbolFirst(initTerminalFirstMap, initNonTerminalFirstMap)
      .map { case (syntacticSymbol: SyntacticSymbol, firstSet: mutable.Set[SyntacticSymbol]) =>
        (syntacticSymbol, firstSet.toSet)
      }
      .toMap
  }

  private def computeAllSymbolFirst(result: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]],
                                    intermediateMap: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]]
                                   ): mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = {
    if (intermediateMap.isEmpty) result
    else {
      val (addableMap, newIntermediateMap) = intermediateMap
        .partition { case (first, _) =>
          result.contains(first) && // 已知其可推导的First集合  存在两种情况1.其为终结符号，任何推导出终结符号的推导可记录到该语法符号的First集合中 2.推导出的非终结符号已经知道其First集合，那么该语法符号也可记录到First集合中
            intermediateMap.values.forall(!_.contains(first)) // 该被推导出来的元素first不可以再推导出其他元素，确保First Set中每一个语法符号对应的First集合中的元素都是终结符号
        }
      val newResultMap = mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]]()
      addableMap
        .map { case (syntacticSymbol: SyntacticSymbol, syntacticSymbolSet: mutable.Set[SyntacticSymbol]) => (syntacticSymbolSet, syntacticSymbol) }
        .map { case (first, second) => (first, result(second)) }
        .foreach { case (first, second) =>
          first.foreach(key => {
            val value = newResultMap.get(key)
            value match {
              case Some(result)=>
                newResultMap += key -> (second ++ result)
              case None =>
                newResultMap += key -> (mutable.Set.empty ++= second)
            }
          })
        }

      newResultMap
    }
    mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]]()
  }

  // (LEFT_BRACE,HashSet(LEFT_BRACE))
  private def computeTerminalSymbolFirst(): mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = {
    val terminalSymbolFirstSet = SyntacticSymbol
      .values
      .filter(SyntacticSymbol.isTerminalSymbol)
      .toVector
      .map(syntacticSymbol => (syntacticSymbol, mutable.Set(syntacticSymbol)))
    mutable.HashMap(terminalSymbolFirstSet: _*)
  }

  private def computeNonTerminalSymbolFirst(): mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = {
    val firstImmutableMap = Grammar.derivationList
      .map { case (nonTerminalSymbol: SyntacticSymbol, symbolVector: Vector[SyntacticSymbol]) =>
        (symbolVector.head, nonTerminalSymbol)
      }
      // 防止非终结符推导出非终结符自身记录到First集合，进入无限循环，没有意义
      .filter { case (first, second) => first != second }
      .groupBy(_._1)
      .map { case (symbol, firstEntryList) => (symbol, mutable.Set(firstEntryList.map(_._2): _*)) }

    mutable.HashMap(firstImmutableMap.toSeq: _*)
  }

}
