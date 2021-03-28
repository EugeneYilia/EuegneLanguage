package common

import common.SyntacticSymbol.{ASSIGN, BLOCK, ELSE_KEYWORD, EXPRESSION, FUNCTION, FUNCTIONS, FUNCTION_KEYWORD, ID, IF_KEYWORD, INT_CONSTANT, LEFT_BRACE, LEFT_PAREN, MINUS, MULTI, PLUS, PRINTLN, RIGHT_BRACE, RIGHT_PAREN, SEMICOLON, STARTER, STATEMENT, STATEMENTS}
import core.LR

// 一个 object 定义可以看成是使用了lazy val定义一个匿名类实例的简化方式
object Grammar {
  val derivationList: DerivationList = List[Derivation](
    STARTER -> Vector(FUNCTIONS),

    FUNCTIONS -> Vector(FUNCTION, FUNCTIONS),
    FUNCTIONS -> Vector(FUNCTION),
    FUNCTION -> Vector(FUNCTION_KEYWORD, ID, LEFT_PAREN, RIGHT_PAREN, BLOCK),

    BLOCK -> Vector(LEFT_BRACE, STATEMENTS, RIGHT_BRACE),

    // 假设存在十个Statements
    // 如果是statements -> statement + statements
    // 那么就是1 9   2 8   3 7    4 6   5 5    6 4   7 3   8 2   9 1 从前靠后一条一条吐出来
    // 如果是statements -> statements + statement
    // 那么就是9 1   8 2   7 3    6 4   5 5    4 6   3 7   2 8   1 9 倒序从后往前一条一条吐出来
    // 导致的结果就是倒序执行代码，以致于代码执行顺序错乱，并不能得出想要的结果
    // 因此采用正序执行代码的方式 为 Statements -> Statement + Statements
    STATEMENTS -> Vector(STATEMENT, STATEMENTS),
    STATEMENTS -> Vector(STATEMENT),

    STATEMENT -> Vector(EXPRESSION, SEMICOLON),

    EXPRESSION -> Vector(INT_CONSTANT),
    EXPRESSION -> Vector(PRINTLN, LEFT_PAREN, EXPRESSION, RIGHT_PAREN),
    EXPRESSION -> Vector(ID),
    EXPRESSION -> Vector(ID, LEFT_PAREN, RIGHT_PAREN),
    EXPRESSION -> Vector(EXPRESSION, PLUS, EXPRESSION),
    EXPRESSION -> Vector(EXPRESSION, MINUS, EXPRESSION),
    EXPRESSION -> Vector(EXPRESSION, MULTI, EXPRESSION),
    EXPRESSION -> Vector(ID, ASSIGN, EXPRESSION),
    EXPRESSION -> Vector(IF_KEYWORD, LEFT_PAREN, EXPRESSION, RIGHT_PAREN, BLOCK, ELSE_KEYWORD, BLOCK)
  )

  // 终结符 可由特定的 非终结符 推导出来，默认带上自身
  // 推导出来指的是在推导式右边的第一个符号，此时其左边的非终结符为该终结符的First文法符号
  var first: () => First = () => LR.computeFirst()

  private var usedDerivationList: List[Derivation] = Grammar.derivationList

  def getUsedDerivationList: List[Derivation] = {
    usedDerivationList
  }

  def setUsedDerivationList(usedDerivationList: List[Derivation]): Unit = {
    this.usedDerivationList = usedDerivationList
  }

  lazy val closureVector: Vector[Closure] = {
    LR.computeItems()
      .toVector
  }

  lazy val closureIndexMap: Map[Closure, State] = {
    LR.computeItems()
      .toVector
      .zipWithIndex
      .toMap
  }

  // for development
  lazy val indexClosureMap: Map[State, Closure] = {
    for ((key, value) <- closureIndexMap) yield (value, key)
  }

}
