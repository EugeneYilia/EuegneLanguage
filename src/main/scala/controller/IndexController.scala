package controller

import com.alibaba.fastjson.JSONObject
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

@RestController
class IndexController {
  @RequestMapping(value = Array("index"))
  def index(): JSONObject = {
    val jsonObject = new JSONObject
    jsonObject.put("code", 0)
    jsonObject.put("data", "success")
    jsonObject
  }


}
