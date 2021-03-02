package common.ASTNode.nonTerminalNode

import common.ASTNode.{Env, ExecResult, Node}

case class StatementsNode(statementList:List[StatementNode]) extends Node {
  override def exec(env: Env): Option[ExecResult] = {
    statementList.foreach(statementNode => statementNode.exec(env))
  }
}