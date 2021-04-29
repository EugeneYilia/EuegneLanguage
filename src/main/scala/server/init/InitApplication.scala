package server.init

import compiler.compilerFront.init.InitCompilerFront
import compiler.compilerFront.io.CodeReader

object InitApplication {
  def initApplication(): Unit = {
    CodeReader.readFromFile("workbench/eugene/Bootstrap.eugene")

    InitCompilerFront.initCompilerFront()
  }
}
