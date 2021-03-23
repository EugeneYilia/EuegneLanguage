package parser.lrParser

import common.Derivation

sealed trait Action

case class Shift(newState: Int) extends Action

case class Reduce(derivation: Derivation) extends Action

case class Accept() extends Action
