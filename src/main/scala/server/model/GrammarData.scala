package server.model

class GrammarData() {
  var grammar: String = _

  def setGrammar(newGrammar: String): Unit = this.grammar = newGrammar
}

class CodeData() {
  var code: String = _

  def setCode(newCode: String): Unit = this.code = newCode
}

class TokenData() {
  var symbol: String = _
  var value: String = _
  var index: Int = _

  def setSymbol(newSymbol: String): Unit = this.symbol = newSymbol

  def setValue(value: String): Unit = this.value = value

  def setIndex(index: Int): Unit = this.index = index
}