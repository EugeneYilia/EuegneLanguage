package compiler.compilerFront.common.ASTNode

class ExecResult(val result: ExpressionNode) {

}

object ExecResult {
  def apply(result: ExpressionNode): ExecResult = new ExecResult(result)
}
