package server.init

import compiler.compilerFront.io.CodeReader

object InitCompiler {
  def initCompiler(): Unit = {
    CodeReader.readFromFile("workbench/eugene/Bootstrap.eugene")
  }
}
