import scala.io.Source

object Bootstrap extends App{
  println(args(0))
  val file = Source.fromFile(args(0))
  val fileContent = file.mkString
  println(fileContent)

}