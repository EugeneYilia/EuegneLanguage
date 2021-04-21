package server.controller

import com.alibaba.fastjson.{JSON, JSONObject}
import compiler.compilerFront.common.Grammar
import compiler.compilerFront.utils.GrammarUtil
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, RestController}
import server.model.GrammarData

// GET 获取
// POST 创建
// PUT 修改
// DELETE 删除
// OPTIONS 查看支持的REST Method

@RestController
class GrammarController {
  @RequestMapping(value = Array("api/grammar"), method = Array(RequestMethod.GET))
  def getGrammar: JSONObject = {
    val jsonObject = new JSONObject
    jsonObject.put("data", GrammarUtil.formatGrammar(Grammar.getUsedDerivationList))
    jsonObject
  }

  @RequestMapping(value = Array("api/grammar"), method = Array(RequestMethod.PUT))
  def updateGrammar(@RequestBody grammar: String): JSONObject = {
    println("original grammar")
    println(grammar)
    println()
    val jsonObject :GrammarData = JSON.parseObject(grammar,classOf[GrammarData])
    val grammarData = jsonObject.grammar

    println("grammarData")
    println(grammarData)
    println()
    val newDerivationList = GrammarUtil.transformGrammar(grammarData)

    println("替换前的文法为:")
    Grammar.getUsedDerivationList.foreach(println)
    println()

    Grammar.setUsedDerivationList(newDerivationList)

    println("替换后的文法为:")
    Grammar.getUsedDerivationList.foreach(println)
    println()

    val returnJson = new JSONObject
    returnJson.put("data", "success")
    returnJson
  }

}
