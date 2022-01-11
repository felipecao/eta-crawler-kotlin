package pe.morena.work.apps.scraper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import java.nio.file.Paths

class CoscoScraperTest {

    init {
        System.setProperty("webdriver.chrome.driver", Paths.get(".").toAbsolutePath().normalize().toString() + "/chromedriver/97.0.4692.71/chromedriver")
    }

    @Test
    fun `findEta works properly`() {
        val driver = ChromeDriver()
        val scraper = CoscoScraper(driver)

        val container = "TGBU8745905"
        val expectedEta = "12.01.22"

        assertThat(scraper.findEta(container)).isEqualTo(expectedEta)
    }
}