package common.ASTNode.nonTerminalNode

import common.ASTNode.{Env, ExecResult, Node}

case class FunctionNode(name:String,statementsNode:StatementsNode) extends Node {
  override def exec(env: Env): Option[ExecResult] = statementsNode.exec(env)

  override def toString: String = s"func $name $statementsNode"
}
