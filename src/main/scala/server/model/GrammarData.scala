package server.model

class GrammarData() {
  var grammar: String = _

  def setGrammar(newGrammar: String): Unit = {
    this.grammar = newGrammar
  }
}

class CodeData() {
  var code: String = _

  def setCode(newCode:String):Unit = {
    this.code = newCode
  }
}