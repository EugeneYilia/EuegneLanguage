package server.controller.lexer

import com.alibaba.fastjson.{JSON, JSONObject}
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, RestController}
import server.model.{TokenData}

@RestController
class TokenController {

  @RequestMapping(value = Array("api/token"), method = Array(RequestMethod.PUT))
  def updateGrammar(@RequestBody data: String): JSONObject = {
    val jsonObject: TokenData = JSON.parseObject(data, classOf[TokenData])
    val value = jsonObject.value
    val index = jsonObject.index
    val symbol = jsonObject.symbol
    println(value)
    println(index)
    println(symbol)
    val returnJson = new JSONObject
    returnJson
  }
}
