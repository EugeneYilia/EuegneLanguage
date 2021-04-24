package server

import org.springframework.boot.SpringApplication
import server.config.AppConfig
import server.init.InitCompiler.initCompiler

object CompilerApplication extends App {
  initCompiler()
  SpringApplication.run(classOf[AppConfig])
}
