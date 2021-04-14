import config.AppConfig
import org.springframework.boot.SpringApplication

object CompilerApplication extends App {
  SpringApplication.run(classOf[AppConfig])
}
