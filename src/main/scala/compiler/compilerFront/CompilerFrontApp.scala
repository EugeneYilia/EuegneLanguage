package compiler.compilerFront

import compiler.compilerFront.io.CodeReader

object CompilerFrontApp extends App {
  println(s"filePath: ")
  println(args(0))
  println()
  val codeReader = CodeReader
  println(s"codeReader: ${codeReader.getCodeContent()}")
  codeReader.readFromFile(args(0))

  Bootstrap.compile()
}
