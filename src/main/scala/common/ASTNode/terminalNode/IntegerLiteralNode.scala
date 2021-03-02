package common.ASTNode.terminalNode

import common.ASTNode.{Env, ExecResult, LiteralNode}

case class IntegerLiteralNode(value:Int) extends LiteralNode {
  override def exec(env: Env): Option[ExecResult] = Some(ExecResult(this))
}
