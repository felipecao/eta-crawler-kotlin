package pe.morena.work.apps.scraper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.chrome.ChromeDriver
import java.nio.file.Paths

class CmaCgmScraperTest {

    init {
        System.setProperty("webdriver.chrome.driver", Paths.get(".").toAbsolutePath().normalize().toString() + "/chromedriver/96.0.4664.45/chromedriver")
    }

    @Test
    fun `findEta works properly`() {
        val scraper = CmaCgmScraper(ChromeDriver())

        val container = "CMAU6290576"
        val expectedEta = "20.12.21"

        assertThat(scraper.findEta(container)).isEqualTo(expectedEta)
    }
}