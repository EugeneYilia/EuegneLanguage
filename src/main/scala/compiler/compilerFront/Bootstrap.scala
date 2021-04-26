package compiler.compilerFront

import compiler.compilerFront.common.ASTNode._
import compiler.compilerFront.common.ASTNode.nonTerminalNode._
import compiler.compilerFront.common.SyntacticSymbol.SyntacticSymbol
import compiler.compilerFront.common._
import compiler.compilerFront.core.LR
import compiler.compilerFront.core.LR._
import compiler.compilerFront.io.CodeReader
import compiler.compilerFront.lexer.Lexer
import compiler.compilerFront.parser.lrParser.LRParser
import server.cache.ResultCache

object Bootstrap {

  def compile(): Option[ExecResult] = {

    ResultCache.clearCache()

    val codeReader = CodeReader
    val codeContent = codeReader.getCodeContent
    println(s"fileContent: ")
    println(codeContent)
    println()

    val tokens = Lexer(codeContent) :+ (SyntacticSymbol.$, null)

    println(s"tokens: ")
    println(tokens.mkString)
    println()

    println("Grammars: ")
    Grammar.getUsedDerivationList.foreach(println)
    println()

    println("First: ")
    Grammar.first().foreach(println)
    println()

    println("Original Closure: ")
    val originalItem: Item = (SyntacticSymbol.STARTER, Vector(), Vector(SyntacticSymbol.FUNCTIONS), SyntacticSymbol.$)
    LR.computeClosure(originalItem).foreach(println)
    println()

    println("Closure Vector: ")
    Grammar.closureVector.foreach(element => {
      println("Closure: ")
      element.foreach(item => {
        println(s"Item: ${item}")
      })
      println()
    })

    println("Analysis Table: ")
    val initDerivation = Grammar.derivationList.filter(_._1 == SyntacticSymbol.STARTER).map(_._2).head
    val initItem = (SyntacticSymbol.STARTER, Vector[SyntacticSymbol](), initDerivation, SyntacticSymbol.$)
    val initClosure = computeClosure(initItem)
    val initState = LR.computeState(initClosure)

    val analysisTable = LR.computeAnalysisTable()
    val actionMap = analysisTable._1
    val gotoMap = analysisTable._2
    println("Action Map")
    actionMap.foreach(println)
    println()
    println("Goto Map")
    gotoMap.foreach(println)
    println()

    val initStateStack = Vector(initState)
    val initNodeStack = Vector[Node](BasicNode(""))

    // 文法 Grammar -> 分析表 AnalysisTable -> 抽象语法树 AST
    val lrParser = LRParser(analysisTable)

    // for debug **
    lrParser.showAnalysisTable()
    // for debug ****

    val (rootNode, leftNodeVector) = lrParser.parse(initStateStack, initNodeStack, tokens.toVector)


    println("*** Eugene Program Execute Result ***")
    val initEnv = Env(None, rootNode.asInstanceOf[FunctionsNode].convertToMap)
    initEnv.get("main") match {
      case Some(node) =>
        node.asInstanceOf[FunctionNode].exec(initEnv)
      case None =>
        System.err.println("main function not exist")
        throw new RuntimeException("main function not exist")
    }
  }
}
