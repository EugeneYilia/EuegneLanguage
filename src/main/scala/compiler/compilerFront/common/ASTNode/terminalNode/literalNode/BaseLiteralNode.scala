package compiler.compilerFront.common.ASTNode.terminalNode.literalNode

import compiler.compilerFront.common.ASTNode.LiteralNode

abstract class BaseLiteralNode(val value: Any) extends LiteralNode {
  override def toString: String = value.toString
}
