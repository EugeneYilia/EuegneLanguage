package common.ASTNode.terminalNode

import common.ASTNode.{Env, ExecResult, ExpressionNode}

case class AssignNode(key: String, value: ExpressionNode) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] =
    value.exec(env) match {
      case Some(execResult: ExecResult) =>
        env.set(key, execResult.result)
        None
      case None =>
        val errorMsg = s"ExpressionNode $value exec return None"
        System.err.println(errorMsg)
        throw new RuntimeException(errorMsg)
    }
}
