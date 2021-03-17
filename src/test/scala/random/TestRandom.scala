package random

import common.{Grammar, Item, SyntacticSymbol}
import common.SyntacticSymbol._
import core.LR
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable
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

  // TODO: fix compute all items bug
  test("computeAllItems") {
    val productionSet = List(
      STARTER -> Vector(EXPRESSION),
      EXPRESSION -> Vector(C, C),
      C -> Vector(c, C),
      C -> Vector(d)
    )
    Grammar.setUsedDerivationList(productionSet)
    val result = LR.computeItems()
    val expected = Set(
      Set((C, Vector(), Vector(d), d), (C, Vector(), Vector(d), c), (EXPRESSION, Vector(), Vector(C, C), $), (C, Vector(), Vector(c, C), d), (C, Vector(), Vector(c, C), c), (STARTER, Vector(), Vector(EXPRESSION), $)), // 0
      Set((STARTER, Vector(EXPRESSION), Vector(), $)), // 1
      Set((EXPRESSION, Vector(C), Vector(C), $), (C, Vector(), Vector(c, C), $), (C, Vector(), Vector(d), $)), // 2
      Set((C, Vector(), Vector(d), d), (C, Vector(), Vector(d), c), (C, Vector(), Vector(c, C), d), (C, Vector(), Vector(c, C), c), (C, Vector(c), Vector(C), c), (C, Vector(c), Vector(C), d)), // 3
      Set((C, Vector(d), Vector(), d), (C, Vector(d), Vector(), c)), // 4
      Set((EXPRESSION, Vector(C, C), Vector(), $)), // 5
      Set((C, Vector(c), Vector(C), $), (C, Vector(), Vector(c, C), $), (C, Vector(), Vector(d), $)), // 6
      Set((C, Vector(d), Vector(), $)), // 7
      Set((C, Vector(c, C), Vector(), d), (C, Vector(c, C), Vector(), c)), // 8
      Set((C, Vector(c, C), Vector(), $)) // 9
    )

    println("result: ")
    println(result)
    println
    println("expected: ")
    println(expected)
    println
    assertResult(expected)(result)
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
