package compiler.compilerFront

import compiler.compilerFront.common.ASTNode.Node
import compiler.compilerFront.common.SyntacticSymbol.SyntacticSymbol

import scala.collection.mutable
import compiler.compilerFront.parser.lrParser.Action

package object common {
  type Token = (SyntacticSymbol, String)

  // Vector类目前可以被认为是这么一个通用的不可变数据结构。Vector是一个带索引的不可变序列容器
  // 如果你更倾向于使用一个链式不可变集合容器，那么你可以选择List

  // 此为文法的推导式子
  type Derivation = (SyntacticSymbol, Vector[SyntacticSymbol])
  type DerivationList = List[Derivation]

  // HashMap的key可由Value中的Set中的语法符号集中的任一个语法符号推导出来
  type First = Map[SyntacticSymbol, Set[SyntacticSymbol]]

  // 等价于一个对应的Closure
  type State = Int

  type Item = (SyntacticSymbol, Vector[SyntacticSymbol], Vector[SyntacticSymbol], SyntacticSymbol)
  // 中间过程项目集合
  type ItemSet = mutable.Set[Item]
  // 最终生成的项目集合闭包
  type Closure = Set[Item]

  type ActionMap = Map[(State, SyntacticSymbol), Action]
  type GotoMap = Map[(State, SyntacticSymbol), State]
  type AnalysisTable = (ActionMap, GotoMap)

  type ParseResult = (Node, Vector[Token])
}
