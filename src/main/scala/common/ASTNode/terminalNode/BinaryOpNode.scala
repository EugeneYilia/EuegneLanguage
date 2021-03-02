package common.ASTNode.terminalNode

import common.ASTNode.{Env, ExecResult, ExpressionNode}

case class BinaryOpNode(operator: Char, left: ExpressionNode, right: ExpressionNode) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] = {
    var leftValue: Int = 0
    var rightValue: Int = 0

    val leftExecResult = left.exec(env)
    leftExecResult match {
      case Some(execResult: ExecResult) => leftValue = execResult.result.asInstanceOf[IntegerLiteralNode].value
      case None => throw new RuntimeException(s"BinaryOpNode left part error: $left")
    }

    val rightExecResult = right.exec(env)
    rightExecResult match {
      case Some(execResult: ExecResult) => rightValue = execResult.result.asInstanceOf[IntegerLiteralNode].value
      case None => throw new RuntimeException(s"BinaryOpNode right part error: $right")
    }

    val resultValue = operator match {
      case '+' => leftValue + rightValue
      case '-' => leftValue - rightValue
      case '*' => leftValue * rightValue
      case _ => throw new RuntimeException(s"BinaryOpNode operator not support now: $operator")
    }
    Some(ExecResult(IntegerLiteralNode(resultValue)))
  }
}
