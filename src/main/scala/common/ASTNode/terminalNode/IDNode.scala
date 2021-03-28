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
        val errMsg = s"IDNode Id: ${name} not exist"
        System.err.println(errMsg)
        throw new RuntimeException(errMsg)
    }

  override def toString: String = this.name
}


