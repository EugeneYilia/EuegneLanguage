package common.ASTNode.terminalNode

import common.ASTNode.{Env, ExecResult, ExpressionNode}

/**
 * Maybe have a better way to handler this node
 */

case class IDNode(name: String) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] =
    env.get(name) match {
      case Some(node) => Some(ExecResult(node.asInstanceOf[ExpressionNode]))
      case None =>
        val errorMsg = s"IDNode Id: ${name} not exist"
        System.err.println(errorMsg)
        throw new RuntimeException(errorMsg)
    }

  override def toString: String = this.name
}


