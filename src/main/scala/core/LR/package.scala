package core

import common.SyntacticSymbol.SyntacticSymbol
import common.{Closure, DerivationList, First, Grammar, Item, ItemSet, SyntacticSymbol}
import utils.ClosureUtil

import scala.annotation.tailrec
import scala.collection.mutable


package object LR {
  def computeAnalysisTable(derivationList: DerivationList): Unit = {
    //    val derivationMap = derivationList.zipWithIndex.toMap

  }

  def computeClosure(item:Item): Closure = {
    val originalItemSet = mutable.Set[Item](item)
    val resultItemSet = mutable.Set[Item]()
    computeAllItems(originalItemSet, resultItemSet).toSet
  }

  @tailrec
  private def computeAllItems(intermediateSet: ItemSet, resultSet: ItemSet): ItemSet = {
    if (intermediateSet.isEmpty) resultSet
    else {
      val newResultSet = resultSet ++ intermediateSet
      val newIntermediateSet = intermediateSet.flatMap(ClosureUtil. originalStep).diff(newResultSet)
      computeAllItems(newIntermediateSet, newResultSet)
    }
  }

  def computeFirst(): First = {
    // 终结符号的First集合只由其自身构成
    val initTerminalFirstMap: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = computeTerminalSymbolFirst()

    // 添加非终结符号的First集合至FirstMap
    val initNonTerminalFirstMap: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = computeNonTerminalSymbolFirst()

    computeAllSymbolFirst(initTerminalFirstMap, initNonTerminalFirstMap)
      .map { case (syntacticSymbol: SyntacticSymbol, firstSet: mutable.Set[SyntacticSymbol]) =>
        (syntacticSymbol, firstSet.toSet)
      }
      .toMap
  }

  @tailrec
  private def computeAllSymbolFirst(result: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]],
                                    intermediateMap: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]]
                                   ): mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = {
    if (intermediateMap.isEmpty) result
    else {
      val (addableMap, newIntermediateMap) = intermediateMap
        .partition { case (first, _) =>
          intermediateMap.values.forall(!_.contains(first)) && // 相当于终结符，不会推导出别的语法符号：该被推导出来的元素first不可以再推导出其他元素，确保First Set中每一个语法符号对应的First集合中的元素都是终结符号
            result.contains(first) // 该等效终结符号已经有对应的First集合：已知其可推导的First集合  存在两种情况1.其为终结符号，任何推导出终结符号的推导可记录到该语法符号的First集合中 2.推导出的非终结符号已经知道其First集合，那么该语法符号也可记录到First集合中
        }
      //      val newAddableResultMap = mutable.HashMap[SyntacticSymbol, mutable.Set[SyntacticSymbol]]()
      addableMap
        .toVector
        .map { case (syntacticSymbol: SyntacticSymbol, syntacticSymbolSet: mutable.Set[SyntacticSymbol]) => (syntacticSymbolSet, syntacticSymbol) }
        .map { case (first, second) => (first, result(second)) } // 通过了上面的filter result中必定存在该key因此可以这么写
        .foreach { case (first, second) =>
          first.foreach(key => {
            val value = result.get(key)
            value match {
              case Some(firstResult) =>
                result += key -> (second ++ firstResult)
              case None =>
                result += key -> (mutable.Set.empty ++= second)
            }
          })
        }
      computeAllSymbolFirst(result, newIntermediateMap)
    }
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
