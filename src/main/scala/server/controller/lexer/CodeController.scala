package server.controller.lexer

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import compiler.compilerFront.common.SyntacticSymbol
import compiler.compilerFront.io.CodeReader
import compiler.compilerFront.lexer.Lexer
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, RestController}
import server.model.CodeData

@RestController
class CodeController {
  @RequestMapping(value = Array("api/code"), method = Array(RequestMethod.GET))
  def getGrammar: JSONObject = {
    val jsonObject = new JSONObject
    jsonObject.put("data", CodeReader.getCodeContent)
    jsonObject
  }

  @RequestMapping(value = Array("api/code"), method = Array(RequestMethod.PUT))
  def updateGrammar(@RequestBody code: String): JSONObject = {
    println("original code")
    println(code)
    println()
    val jsonObject: CodeData = JSON.parseObject(code, classOf[CodeData])
    val codeData = jsonObject.code

    println("codeData")
    println(codeData)
    println()

    println("替换前的代码为:")
    println(CodeReader.getCodeContent)

    CodeReader.setCodeContent(codeData)

    println("替换后的代码为:")
    println(CodeReader.getCodeContent)

    val tokens = Lexer(codeData) :+ (SyntacticSymbol.$, null)
    println(s"tokens: ${tokens}")
    val returnJson = new JSONObject
    returnJson.put("data", "success")
    val jsonArray = new JSONArray()
    tokens.foreach(token => {
      val jsonObject = new JSONObject()
      jsonObject.put("symbol", token._1.toString)
      jsonObject.put("value", if (token._2 == null) "" else token._2)
      jsonArray.add(jsonObject)
    })

    returnJson.put("tokens", JSON.toJSONString(jsonArray))
    returnJson
  }
}
