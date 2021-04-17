package random

import compiler.compilerFront.common.{Closure, Grammar, Item, SyntacticSymbol}
import compiler.compilerFront.common.SyntacticSymbol._
import compiler.compilerFront.core.LR
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
    val newResult = result + ("aaa" -> List(3, 4))
    println(newResult)
  }
  // Result:
  // Map(aaa -> List(1, 2, 3))
  // Map(aaa -> List(3, 4))
  // 会对之前结果进行覆盖

  test("test ->") {
    val x = "xxx" -> "yyy" // 构成tuple元组()   结果为("xxx","yyy")
    println(x)
    val y = List(x)
    println(y)
  }

  test("test fetch element in map") {
    val map = mutable.Map[String, String]()
    map += ("a" -> "b")
    println(map)
    println(map("a")) // 打印出b
    //    println(map("c"))// 报错java.util.NoSuchElementException: key not found: c
    println()
    println(map.get("a")) // Some(b)
    println(map.get("c")) // None
  }
  // 由上述可见，从Map中取元素的时候最好采用map.get(key)的方式来获取value比较好，对结果进行pattern match然后进行操作最优雅


  test("test +: in vector") {
    val vector = Vector(1, 2, 3)
    val newElement = 4
    val newVector = newElement +: vector
    println(newVector) // Vector(4, 1, 2, 3)
    println(newVector.head) // 4
    println(newVector(0)) // 4
  }

  test("test tail") {
    val vector = Vector(1, 2, 3, 4, 5, 6)
    println(vector) // Vector(1, 2, 3, 4, 5, 6)
    println(vector.head) // 1
    println(vector.tail) // Vector(2, 3, 4, 5, 6)
  }

  test("test drop") {
    val vector = Vector(1, 2, 3, 4, 5, 6)
    println(vector.drop(3)) // Vector(4, 5, 6)
    // Drop掉前n个，返回剩下的元素构成的Vector
  }

  test("test splitAt") {
    val vector = Vector(1, 2, 3, 4, 5, 6)
    println(vector.splitAt(3)) // (Vector(1, 2, 3),Vector(4, 5, 6))
    // 分成两个Vector，由前n个元素构成的第一个Vector和从第n+1个开始的元素到最后一个元素构成的第二个Vector
  }


  test("test ???") {
    val x = new TestX()
    val result = x.test() // val result: Int  该行代码会报错：scala.NotImplementedError: an implementation is missing
    println(result)
  }

  test("test trim") {
    val x = "  Fucking lies  "
    println(x)
    println(x.trim)
  }

  class TestX {
    def test(): Int = ???
  }
  // 对???方法的使用会报如下错误 ↓👇
  //an implementation is missing
  //scala.NotImplementedError: an implementation is missing
  //	at scala.Predef$.$qmark$qmark$qmark(Predef.scala:345)
  //	at random.TestRandom$TestX.test(TestRandom.scala:184)
  //	at random.TestRandom.$anonfun$new$29(TestRandom.scala:179)
  //	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
  //	at org.scalatest.OutcomeOf.outcomeOf(OutcomeOf.scala:85)
  //	at org.scalatest.OutcomeOf.outcomeOf$(OutcomeOf.scala:83)
  //	at org.scalatest.OutcomeOf$.outcomeOf(OutcomeOf.scala:104)
  //	at org.scalatest.Transformer.apply(Transformer.scala:22)
  //	at org.scalatest.Transformer.apply(Transformer.scala:20)
  //	at org.scalatest.funsuite.AnyFunSuiteLike$$anon$1.apply(AnyFunSuiteLike.scala:190)
  //	at org.scalatest.TestSuite.withFixture(TestSuite.scala:196)
  //	at org.scalatest.TestSuite.withFixture$(TestSuite.scala:195)
  //	at org.scalatest.funsuite.AnyFunSuite.withFixture(AnyFunSuite.scala:1563)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.invokeWithFixture$1(AnyFunSuiteLike.scala:188)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.$anonfun$runTest$1(AnyFunSuiteLike.scala:200)
  //	at org.scalatest.SuperEngine.runTestImpl(Engine.scala:306)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.runTest(AnyFunSuiteLike.scala:200)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.runTest$(AnyFunSuiteLike.scala:182)
  //	at org.scalatest.funsuite.AnyFunSuite.runTest(AnyFunSuite.scala:1563)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.$anonfun$runTests$1(AnyFunSuiteLike.scala:233)
  //	at org.scalatest.SuperEngine.$anonfun$runTestsInBranch$1(Engine.scala:413)
  //	at scala.collection.immutable.List.foreach(List.scala:333)
  //	at org.scalatest.SuperEngine.traverseSubNodes$1(Engine.scala:401)
  //	at org.scalatest.SuperEngine.runTestsInBranch(Engine.scala:396)
  //	at org.scalatest.SuperEngine.runTestsImpl(Engine.scala:475)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.runTests(AnyFunSuiteLike.scala:233)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.runTests$(AnyFunSuiteLike.scala:232)
  //	at org.scalatest.funsuite.AnyFunSuite.runTests(AnyFunSuite.scala:1563)
  //	at org.scalatest.Suite.run(Suite.scala:1112)
  //	at org.scalatest.Suite.run$(Suite.scala:1094)
  //	at org.scalatest.funsuite.AnyFunSuite.org$scalatest$funsuite$AnyFunSuiteLike$$super$run(AnyFunSuite.scala:1563)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.$anonfun$run$1(AnyFunSuiteLike.scala:237)
  //	at org.scalatest.SuperEngine.runImpl(Engine.scala:535)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.run(AnyFunSuiteLike.scala:237)
  //	at org.scalatest.funsuite.AnyFunSuiteLike.run$(AnyFunSuiteLike.scala:236)
  //	at org.scalatest.funsuite.AnyFunSuite.run(AnyFunSuite.scala:1563)
  //	at org.scalatest.tools.SuiteRunner.run(SuiteRunner.scala:45)
  //	at org.scalatest.tools.Runner$.$anonfun$doRunRunRunDaDoRunRun$13(Runner.scala:1320)
  //	at org.scalatest.tools.Runner$.$anonfun$doRunRunRunDaDoRunRun$13$adapted(Runner.scala:1314)
  //	at scala.collection.immutable.List.foreach(List.scala:333)
  //	at org.scalatest.tools.Runner$.doRunRunRunDaDoRunRun(Runner.scala:1314)
  //	at org.scalatest.tools.Runner$.$anonfun$runOptionallyWithPassFailReporter$24(Runner.scala:993)
  //	at org.scalatest.tools.Runner$.$anonfun$runOptionallyWithPassFailReporter$24$adapted(Runner.scala:971)
  //	at org.scalatest.tools.Runner$.withClassLoaderAndDispatchReporter(Runner.scala:1480)
  //	at org.scalatest.tools.Runner$.runOptionallyWithPassFailReporter(Runner.scala:971)
  //	at org.scalatest.tools.Runner$.run(Runner.scala:798)
  //	at org.scalatest.tools.Runner.run(Runner.scala)
  //	at org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner.runScalaTest2or3(ScalaTestRunner.java:38)
  //	at org.jetbrains.plugins.scala.testingSupport.scalaTest.ScalaTestRunner.main(ScalaTestRunner.java:25)


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
