package server.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.{CorsRegistry, WebMvcConfigurationSupport}

@Configuration
class CorsConfig extends WebMvcConfigurationSupport {
  override def addCorsMappings(corsRegistry: CorsRegistry) = {
    corsRegistry
      .addMapping("/**")
      .allowedOrigins("*")
      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
      .allowedHeaders("*")
    super.addCorsMappings(corsRegistry)
  }
}
