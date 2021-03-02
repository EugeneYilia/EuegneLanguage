import common.SyntacticSymbol.SyntacticSymbol

package object common {
  type Token = (SyntacticSymbol, String)

  // Vector类目前可以被认为是这么一个通用的不可变数据结构。Vector是一个带索引的不可变序列容器
  // 如果你更倾向于使用一个链式不可变集合容器，那么你可以选择List

  // 此为文法的推导式子
  type Derivation = (SyntacticSymbol, Vector[SyntacticSymbol])
  type DerivationList = List[Derivation]


}
