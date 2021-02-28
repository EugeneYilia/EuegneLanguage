package common.ASTNode.nonTerminalNode

import common.ASTNode.{Env, ExecResult, Node}

case class BasicNode(value:String) extends Node {
  override def exec(env: Env): Option[ExecResult] = throw new RuntimeException("BasicNode can't execute.")

  override def toString: String = value
}
