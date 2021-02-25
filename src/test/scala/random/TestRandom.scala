package random

import org.scalatest.funsuite.AnyFunSuite

class TestRandom extends AnyFunSuite {
  test("test::") {
    val source = List('1', '2', '3', '4', '5')
    source match {
      case a :: b :: c => {
        println(a)
        println(b)
        println(c)
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
}
