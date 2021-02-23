package lexer

object Lexer {
  // span 使用bool表达式进行切割，遇到第一个不满足的元素就返回，形成两个List，从开头至中断处满足bool表达式的第一个List，以及从不满足bool表达式的第一个元素至之前列表最后一个元素构成的List
  // 在这里取跳过的所有无效字符的新List作为结果返回
  // 确保从字符串中取字符的时候 其之前的无效字符都已经被滤掉
  def skipWhiteSpace(sourceCharList:List[Char]) :List[Char] = sourceCharList.span(_.isWhitespace)._2

  //针对@tailrec注解的使用需要注意
  //@scala.annotation.tailrec，这个符号除了可以标识尾递归外，更重要的是编译器会检查该函数是否真的尾递归，若不是，会导致如下编译错误。
  //could not optimize @tailrec annotated method fibonacci: it contains a recursive call not in tail position

}
