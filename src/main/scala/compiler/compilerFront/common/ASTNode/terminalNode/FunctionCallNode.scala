package compiler.compilerFront.common.ASTNode.terminalNode

import compiler.compilerFront.common.ASTNode.nonTerminalNode.FunctionNode
import compiler.compilerFront.common.ASTNode.{Env, ExecResult, ExpressionNode}

case class FunctionCallNode(name: String) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] =
    env.get(name) match {
      case Some(node) =>
        node.asInstanceOf[FunctionNode].exec(env)
      case None =>
        val errorMsg = s"FunctionCallNode Id: $name not exist"
        System.err.println(errorMsg)
        throw new RuntimeException(errorMsg)
    }

  override def toString: String = this.name
}
