package server

import org.springframework.boot.SpringApplication
import server.config.AppConfig
import server.init.InitApplication

object CompilerApplication extends App {
  InitApplication.initApplication()
  SpringApplication.run(classOf[AppConfig])
}
