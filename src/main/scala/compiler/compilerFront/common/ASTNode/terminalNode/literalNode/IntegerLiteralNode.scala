package compiler.compilerFront.common.ASTNode.terminalNode.literalNode

import compiler.compilerFront.common.ASTNode.{Env, ExecResult}

case class IntegerLiteralNode(override val value: Int) extends BaseLiteralNode {
  override def exec(env: Env): Option[ExecResult] = Some(ExecResult(this))
}
