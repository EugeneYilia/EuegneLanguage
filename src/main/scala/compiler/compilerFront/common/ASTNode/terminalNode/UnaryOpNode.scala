package compiler.compilerFront.common.ASTNode.terminalNode

import compiler.compilerFront.common.ASTNode.terminalNode.literalNode.IntegerLiteralNode
import compiler.compilerFront.common.ASTNode.{Env, ExecResult, ExpressionNode}

case class UnaryOpNode(operator:Char,expressionNode:ExpressionNode) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] = operator match {
    case '-' =>
      val expressionValue = expressionNode.exec(env)
      expressionValue match {
        case Some(execResult: ExecResult) => Some(ExecResult(IntegerLiteralNode(execResult.result.asInstanceOf[IntegerLiteralNode].value)))
        case None => None
      }
    case _ => None
  }

  override def toString: String = s"$operator$expressionNode"
}
