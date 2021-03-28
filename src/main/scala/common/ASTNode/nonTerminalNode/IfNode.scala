package common.ASTNode.nonTerminalNode

import common.ASTNode.terminalNode.literalNode.IntegerLiteralNode
import common.ASTNode.{Env, ExecResult, ExpressionNode, Node}

import scala.collection.mutable

case class IfNode(condition: ExpressionNode, passStatements: StatementsNode, failStatements: StatementsNode) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] = {
    condition.exec(env) match {
      case Some(execResult) =>
        if (execResult.result.asInstanceOf[IntegerLiteralNode].value != 0) {
          // 执行结束完statementsNode，自动将局部Env丢弃掉
          passStatements.exec(Env(Some(env), mutable.Map[String, Node]()))
        } else {
          failStatements.exec(Env(Some(env), mutable.Map[String, Node]()))
        }
      case None =>
        val errMsg = s"IFNode condition: $condition exec return None"
        System.err.println(errMsg)
        throw new RuntimeException(errMsg)
    }
  }
}
