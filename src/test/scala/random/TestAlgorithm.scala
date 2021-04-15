package random

import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable

class TestAlgorithm extends AnyFunSuite {
  test("test dice sum") {
    val originalData = Array(1, 2, 3, 4, 5, 6)
    val sumMap = new mutable.HashMap[Int, Int]

    for (element1 <- originalData) {
      for (element2 <- originalData) {
        for (element3 <- originalData) {
          val sum = element1 + element2 + element3
          sumMap.get(sum) match {
            case Some(value) =>
              sumMap += (sum -> (value + 1))
            case None =>
              sumMap += (sum -> 1)
          }
        }
      }
    }
    val allCount = sumMap.values.sum
    println(s"allCount: ${allCount}")
    sumMap.foreach(entry => {
      val key = entry._1
      val value = entry._2
      val percentage =((BigDecimal(value) / BigDecimal(allCount)).floatValue * 100).formatted("%.2f") + "%"
      println(s"$key:  $value  $percentage")
    })
  }
}
