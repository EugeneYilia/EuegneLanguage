package compiler.compilerFront.io

object ResultCache {
  private val result: StringBuilder = new StringBuilder

  def clearCache(): Unit = {
    this.result.clear()
  }

  def appendResultContent(newResultContent: String): Unit = {
    this.result.append(newResultContent)
  }

  def getResultContent: String = {
    this.result.toString()
  }
}
