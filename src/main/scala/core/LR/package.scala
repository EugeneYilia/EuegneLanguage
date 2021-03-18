package core

import common.SyntacticSymbol.SyntacticSymbol
import common._
import utils.{ClosureUtil, GotoUtil}

import scala.annotation.tailrec
import scala.collection.mutable


package object LR {
  //TODO: 1.思考文法关于Statements的构造 2.完成分析表的计算
  def computeAnalysisTable(): Unit = {
    //    val derivationMap = derivationList.zipWithIndex.toMap
    val closureVector = computeItems().toVector
    println("closureVector: ")
    closureVector.foreach(println)
    println()
  }


  // 根据项目的产生式计算出所有的项目闭包
  def computeItems(): Set[Closure] = {

    // Bug 所在处
    @tailrec
    def computeAllItems(intermediateSet: Set[Closure], resultSet: Set[Closure]): Set[Closure] = {
      if (intermediateSet.isEmpty) resultSet
      else {
        val newResult = resultSet ++ intermediateSet
        val newIntermediateSetTemp = for {
          closure: Closure <- intermediateSet
          syntacticSymbol: SyntacticSymbol <- SyntacticSymbol.values.toSet
          newClosure = GotoUtil.goto(closure, syntacticSymbol)
          if newClosure.nonEmpty
        } yield newClosure

        val newIntermediateSet = newIntermediateSetTemp.diff(newResult)
        computeAllItems(newIntermediateSet, newResult)
      }
    }

    //    val starterItem: Item = (SyntacticSymbol.STARTER, Vector(), Vector(SyntacticSymbol.FUNCTIONS), SyntacticSymbol.$)
    val starterItem: Item = (SyntacticSymbol.STARTER, Vector(), Grammar.getUsedDerivationList.filter(_._1 == SyntacticSymbol.STARTER).map(_._2).head, SyntacticSymbol.$)
    val startClosure = Set[Closure](computeClosure(starterItem))
    computeAllItems(startClosure, Set[Closure]())
  }

  def computeClosure(item: Item): Closure = {

    // If Input -> (Starter, Vector(), Vector(Functions), $)
    // 1. (Functions, Vector(), Vector(Functions, Function), $)
    //    (Functions, Vector(), Vector(Function), $)
    // 2. (Functions, Vector(), Vector(Functions, Function), Function_Keyword)
    //    (Functions, Vector(), Vector(Function), Function_Keyword)
    //    (Function, Vector(), Vector(Function_keyword,id, l_paren, r_paren, block), $)
    // 3. (Function, Vector(), Vector(Function_keyword,id, l_paren, r_paren, block), Function_keyword)
    @tailrec
    def computeAllItems(intermediateSet: ItemSet, resultSet: ItemSet, derivationList: List[Derivation] = Grammar.getUsedDerivationList): ItemSet = {
      if (intermediateSet.isEmpty) resultSet
      else {
        val newResultSet = resultSet ++ intermediateSet
        val newIntermediateSet = intermediateSet.flatMap(ClosureUtil.singleStep(_, derivationList)).diff(newResultSet)
        computeAllItems(newIntermediateSet, newResultSet)
      }
    }

    val originalItemSet = mutable.Set[Item](item)
    val resultItemSet = mutable.Set[Item]()
    computeAllItems(originalItemSet, resultItemSet).toSet
  }

  def computeFirstSet(derivationLeft: Vector[SyntacticSymbol]): Option[Set[SyntacticSymbol]] = {
    if (derivationLeft.isEmpty) None
    else Some(Grammar.first()(derivationLeft.head))
  }

  def computeFirst(derivationList: List[Derivation] = Grammar.getUsedDerivationList): First = {

    // (LEFT_BRACE,HashSet(LEFT_BRACE))
    def computeTerminalSymbolFirst(): mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = {
      val terminalSymbolFirstSet = SyntacticSymbol
        .values
        .filter(SyntacticSymbol.isTerminalSymbol)
        .toVector
        .map(syntacticSymbol => (syntacticSymbol, mutable.Set(syntacticSymbol)))
      mutable.HashMap(terminalSymbolFirstSet: _*)
    }

    def computeNonTerminalSymbolFirst(): mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]] = {
      val firstImmutableMap = derivationList
        .map { case (nonTerminalSymbol: SyntacticSymbol, symbolVector: Vector[SyntacticSymbol]) =>
          (symbolVector.head, nonTerminalSymbol)
        }
        // 防止非终结符推导出非终结符自身记录到First集合，进入无限循环，没有意义
        .filter { case (first, second) => first != second }
        .groupBy(_._1)
        .map { case (symbol, firstEntryList) => (symbol, mutable.Set(firstEntryList.map(_._2): _*)) }

      mutable.HashMap(firstImmutableMap.toSeq: _*)
    }

    @tailrec
    def computeAllSymbolFirst(result: mutable.Map[SyntacticSymbol, mutable.Set[SyntacticSymbol]],
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
}
