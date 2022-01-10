package pe.morena.work.apps.config

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebDriverConfig {

    @Bean
    fun chromeDriver(): ChromeDriver {
        val options = ChromeOptions()
        // options.addArguments("--disable-gpu", "--headless")
        return ChromeDriver(options)
    }
}