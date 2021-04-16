package server.controller

import com.alibaba.fastjson.JSONObject
import compiler.compilerFront.common.{Derivation, DerivationList, Grammar}
import compiler.compilerFront.utils.GrammarUtil
import org.springframework.web.bind.annotation.{RequestBody, RequestMapping, RequestMethod, RequestParam, RestController}

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
    val jsonObject = new JSONObject()
    val newDerivationList = GrammarUtil.transformGrammar(grammar)
    Grammar.setUsedDerivationList(newDerivationList)
    jsonObject.put("data","success")
    jsonObject
  }



}
