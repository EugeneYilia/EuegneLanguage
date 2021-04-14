package server.controller

import com.alibaba.fastjson.JSONObject
import org.springframework.web.bind.annotation.{RequestMapping, RestController}

import javax.servlet.http.{Cookie, HttpServletResponse}

@RestController
class IndexController {
  @RequestMapping(value = Array("index"))
  def index(httpServletResponse: HttpServletResponse): JSONObject = {
    val cookie = new Cookie("A", "B")
    cookie.setPath("/")
    httpServletResponse.addCookie(cookie)
    val jsonObject = new JSONObject
    jsonObject.put("code", 0)
    jsonObject.put("data", "success")
    jsonObject
  }


}
