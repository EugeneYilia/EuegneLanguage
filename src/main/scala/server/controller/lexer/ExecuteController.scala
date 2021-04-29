package server.controller.lexer

import com.alibaba.fastjson.JSONObject
import compiler.compilerFront.Bootstrap
import compiler.compilerFront.io.{CodeReader, ResultCache}
import org.springframework.web.bind.annotation.{RequestMapping, RequestMethod, RestController}

@RestController
class ExecuteController {
  @RequestMapping(value = Array("api/code/execute"), method = Array(RequestMethod.GET))
  def getGrammar: JSONObject = {
    Bootstrap.compile()
    val jsonObject = new JSONObject
    jsonObject.put("data", ResultCache.getResultContent)
    jsonObject
  }
}
