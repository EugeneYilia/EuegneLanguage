package compiler.compilerFront.common.ASTNode.nonTerminalNode

import compiler.compilerFront.common.ASTNode.{Env, ExecResult, ExpressionNode, Node}

// 一个StatementNode就是一个ExpressionNode,具体的执行委托给其中的expressionNode去执行
case class StatementNode(expressionNode:ExpressionNode) extends Node {
  override def exec(env: Env): Option[ExecResult] = expressionNode.exec(env)

  override def toString: String = expressionNode.toString
}
