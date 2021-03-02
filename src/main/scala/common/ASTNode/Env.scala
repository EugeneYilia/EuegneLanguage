package common.ASTNode

import scala.annotation.tailrec
import scala.collection.mutable

// 符号表
// 取变量nodeMap的时候只能往上取值，不能往下取值
final class Env(val upperEnv:Option[Env],val nodeMap:mutable.Map[String,Node]) {

  // 当前环境有则从当前环境中去，如果当前环境中没有不断递归从父环境中取值
  @tailrec
  def get(key:String): Option[Node] = {
    nodeMap.get(key) match {
      case None =>
        upperEnv match {
          case Some(env) => env.get(key)
          case None => None
        }
      case existNode => existNode
    }
  }

  // 认为已经拿到了对应的Env环境，因此可以直接改该Env下的nodeMap
  def set(key:String,value:Node) : Env = {
    nodeMap += (key->value)
    this
  }

  //在父类中的final方法声明不能被覆盖。 如果不想让它被覆盖，则可以把方法定义成为final。尝试覆盖final方法将导致编译时错误。

  // 只能移除当前Env下的key  不能移除父级的Env中的nodeMap的key
  def remove(key:String) : Env = {
    nodeMap -= key
    this
  }
}

object Env {
  def apply(upperEnv:Option[Env],nodeMap:mutable.Map[String,Node]):Env =
    new Env(upperEnv,nodeMap)
}
