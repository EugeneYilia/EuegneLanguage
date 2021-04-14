package compiler.compilerFront.parser.lrParser

import compiler.compilerFront.common.Derivation

sealed trait Action

case class Shift(newState: Int) extends Action

case class Reduce(derivation: Derivation) extends Action

case class Accept() extends Action
