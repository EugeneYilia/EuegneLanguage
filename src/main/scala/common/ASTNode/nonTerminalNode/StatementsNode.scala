package common.ASTNode.nonTerminalNode

import common.ASTNode.{Env, ExecResult, Node}

case class StatementsNode(statementList:List[StatementNode]) extends Node {
  override def exec(env: Env): Option[ExecResult] = {
    val lastIndex = statementList.size -1
    for(index <- 0 until lastIndex){
      statementList(index).exec(env)
    }
    statementList(lastIndex).exec(env)
  }
}