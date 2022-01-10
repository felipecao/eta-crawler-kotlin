package pe.morena.work.apps

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import java.nio.file.Paths

@SpringBootApplication
@EnableRetry
class MorenaApps

fun main(args: Array<String>) {
	System.setProperty("webdriver.chrome.driver", Paths.get(".").toAbsolutePath().normalize().toString() + "/chromedriver/97.0.4692.71/chromedriver")
	runApplication<MorenaApps>(*args)
}
