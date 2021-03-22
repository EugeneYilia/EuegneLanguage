package random

import common.{Closure, Grammar, Item, SyntacticSymbol}
import common.SyntacticSymbol._
import core.LR
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable.HashSet
import scala.collection.{immutable, mutable}
import scala.collection.mutable.ListBuffer

class TestRandom extends AnyFunSuite {
  test("test::") {
    val source = List('1', '2', '3', '4', '5')
    source match {
      case a :: b :: c => {
        println(a)
        println(b)
        println(c)
      }
      case _ => {

      }

    }
  }

  test("test orElse") {
    val x = None
    val result1 = x.orElse(None)
      .orElse(None)

    println(s"result1:$result1")

    val y = None
    val result2 = y.orElse(None)
      .orElse(Some(1))
      .orElse(None)
    println(s"result2:$result2")
  }

  test("test isLetter") {
    val x = 'ʼ'
    println(x.isLetter)
  }

  test("test Match") {
    val temp = List[Char]('1', '2', 1, 'x')
    temp.foreach(element => println(s"$element:${matchDigit(element)}"))

    val temp2 = "121x".toList
    temp2.foreach(element => println(s"$element:${matchDigit(element)}"))
  }

  test("test +:") {
    println('x' +: List('A', 'B'))
    //    println('x'  List('A','B'))

  }

  test("tst :+") {

  }

  test("receive tuple") {
    val receive1, receive2 = returnTuple
    println(s"receive1:$receive1")
    println(s"receive2:$receive2")
    receive1._1.addOne('X')
    println(s"receive1:$receive1")
    println(s"receive2:$receive2")

    val receive3, (receive4, receive5) = returnTuple
    println(s"receive3:$receive3")
    println(s"receive4:$receive4")
    println(s"receive5:$receive5")
  }

  test("immutableMap operation") {
    val immutableMap = mutable.Map[String, String]()
    println(immutableMap += ("a" -> "a"))
    println(immutableMap += ("a" -> "b"))
    println(immutableMap += ("b" -> "c"))
  }

  test("zipWithIndex") {
    val list = List("A", "B", "C")
    val newList = list.zipWithIndex
    println(newList)
  }

  test("flapMap") {
    val list = List((1, "A"), (2, "B"), (3, "C"))
    val newList = list
      .flatMap(x => x._1.toString)

    println(newList)

  }

  test(":+") {
    val item: Item = (SyntacticSymbol.STARTER, Vector(), Vector(SyntacticSymbol.$), SyntacticSymbol.$)
    item match {
      case (x1, x2, y3 +: x3, x4) => println("Success")
      case _ => println("Failed")
    }
  }

  test("test folder left") {
    val initList = Vector(0, 1, 2, 3, 4)
    val result = initList.foldLeft("")((result, data) => {
      result + 2 * data
    })
    println(result)
  }

  test("operate map") {
    val map = Map[String, List[Int]]()
    val result = map + ("aaa" -> List(1, 2, 3))
    println(result)
    val newResult = map + ("bbb" -> List(3, 4))
    println(newResult)
  }

  def returnTuple: (ListBuffer[Char], ListBuffer[Char]) = {
    (ListBuffer[Char]('A', 'B'), ListBuffer[Char]('C', 'D'))
  }

  def matchIdContent(sourceChar: Char): Boolean = {
    (sourceChar >= 'A' && sourceChar <= 'Z') || (sourceChar >= 'a' && sourceChar <= 'z') || (sourceChar >= '0' && sourceChar <= '9')
  }

  def matchDigit(sourceChar: Char): Boolean = {
    (sourceChar >= '0' && sourceChar <= '9')
  }


}
