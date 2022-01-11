package pe.morena.work.apps.config

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class WebDriverConfig {

    @Bean
    fun chromeDriver(): ChromeDriver {
        val options = ChromeOptions()
        // options.addArguments("--disable-gpu", "--headless")

        val driver = ChromeDriver(options)

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver
    }
}