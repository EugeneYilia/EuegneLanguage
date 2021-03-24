package common.ASTNode.nonTerminalNode

import common.ASTNode.{Env, ExecResult, Node}

import scala.collection.mutable

case class FunctionsNode(functionList: List[FunctionNode]) extends Node {
  override def exec(env: Env): Option[ExecResult] = None

  override def toString: String = functionList.mkString("\n")

  def convertToMap: mutable.Map[String, Node] = mutable.Map(functionList.map(function => function.name -> function): _*)
}
