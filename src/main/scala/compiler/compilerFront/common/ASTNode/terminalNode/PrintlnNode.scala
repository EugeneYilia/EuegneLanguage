package compiler.compilerFront.common.ASTNode.terminalNode

import compiler.compilerFront.common.ASTNode.{Env, ExecResult, ExpressionNode}

case class PrintlnNode(value:ExpressionNode) extends ExpressionNode {
  override def exec(env: Env): Option[ExecResult] = {
    value.exec(env) match {
      // 若有执行结果则打印 否则直接返回None
      case Some(execResult: ExecResult) => println(execResult.result)
      case None => None
    }
    None
  }
}
