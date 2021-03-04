package parser.lrParser

import common.Derivation

sealed trait Action

case class Shift(value: Int) extends Action

case class Reduce(value: Derivation) extends Action

case class Accept() extends Action
