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
//    httpServletResponse.sendRedirect("http://localhost:3000/index/a/b") // ajax请求，所以浏览器不会自动跳转，需要根据返回码，浏览器端控制跳转
    val jsonObject = new JSONObject
    jsonObject.put("code", 0)
    jsonObject.put("data", "success")
    jsonObject
  }


}
