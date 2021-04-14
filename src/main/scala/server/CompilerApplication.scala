package server

import org.springframework.boot.SpringApplication
import server.config.AppConfig

object CompilerApplication extends App {
  SpringApplication.run(classOf[AppConfig])
}
