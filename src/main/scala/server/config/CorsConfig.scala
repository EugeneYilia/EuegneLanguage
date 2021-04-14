package server.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{CorsRegistry, WebMvcConfigurationSupport}

@Configuration
class CorsConfig extends WebMvcConfigurationSupport {
  override def addCorsMappings(corsRegistry: CorsRegistry) = {
    corsRegistry
      .addMapping("/**") //对应的请求路径
      .allowedOriginPatterns("*")//允许哪些源网站访问
      .allowedMethods("*") //允许何种方式访问
      .allowCredentials(true) //是否浏览器应该发送credentials，例如cookies          Access-Control-Allow-Credentials
      .maxAge(7200)
    super.addCorsMappings(corsRegistry)
  }
}
