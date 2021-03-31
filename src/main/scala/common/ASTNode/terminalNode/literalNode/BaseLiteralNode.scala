package common.ASTNode.terminalNode.literalNode

import common.ASTNode.LiteralNode

abstract class BaseLiteralNode(val value: Any) extends LiteralNode {
  override def toString: String = value.toString
}
