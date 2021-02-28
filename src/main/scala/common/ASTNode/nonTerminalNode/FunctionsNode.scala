package common.ASTNode.nonTerminalNode

import common.ASTNode.{Env, ExecResult, Node}

case class FunctionsNode(functionList:List[FunctionNode]) extends Node{
  override def exec(env: Env): Option[ExecResult] = None

  override def toString:String = functionList.mkString("\n")

  def convertToMap : Map[String,FunctionNode] = functionList.map(function => function.name -> function).toMap
}
