package common.ASTNode.terminalNode

import common.ASTNode.nonTerminalNode.FunctionNode
import common.ASTNode.{Env, ExecResult, ExpressionNode}

case class FunctionCallNode(name: String) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] =
    env.get(name) match {
      case Some(node) =>
        node.asInstanceOf[FunctionNode].exec(env)
      case None =>
        val errMsg = s"FunctionCallNode Id: ${name} not exist"
        System.err.println(errMsg)
        throw new RuntimeException(errMsg)
    }

  override def toString: String = this.name
}
