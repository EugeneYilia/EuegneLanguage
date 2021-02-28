package common.ASTNode

trait Node {
  def exec(env:Env): Option[ExecResult]
}
