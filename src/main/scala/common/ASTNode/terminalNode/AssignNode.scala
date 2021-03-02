package common.ASTNode.terminalNode

import common.ASTNode.{Env, ExecResult, ExpressionNode}

case class AssignNode(key:String,value:ExpressionNode) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] = {
    env.set(key,value)
    None
  }
}
