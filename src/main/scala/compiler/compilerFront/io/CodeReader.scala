package compiler.compilerFront.io


import scala.io.Source

/**
 * 获取前必须要先进行代码内容的设置
 */
object CodeReader {
  private var codeContent: String = ""

  def readFromFile(sourcePath:String): Unit ={
    val file = Source.fromFile(sourcePath)
    val fileContent = file.mkString
    this.codeContent = fileContent
  }

  def getCodeContent(): String = {
    this.codeContent
  }

  def setCodeContent(newCodeContent: String): Unit = {
    this.codeContent = newCodeContent
  }
}
