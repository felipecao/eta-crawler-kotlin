package pe.morena.work.apps.scraper

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class CmaCgmScraper(private val driver: ChromeDriver) : Scraper {

    private val wait = WebDriverWait(driver, 10)

    companion object {
        private val gaCookie = "_ga"
        private val allowAll = By.id("onetrust-accept-btn-handler")
        private val carrierCode = "CMA-CGM"
        private val reference = By.id("Reference")
        private val btnTracking = By.id("btnTracking")
        private val eta = By.xpath("//table/tbody/tr[last()]/td[1]/div/span[1]")
    }

    @Retryable(
        include = [NoSuchElementException::class],
        backoff = Backoff(delay = 1000),
        maxAttempts = 3
    )
    override fun findEta(containerNumber: String) : String {
        driver.navigate().to("https://www.cma-cgm.com/ebusiness/tracking")

        if (driver.manage().getCookieNamed(gaCookie) == null) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(allowAll))
            driver.findElement(allowAll).click()
        }

        driver.findElement(reference).sendKeys(containerNumber)
        driver.findElement(btnTracking).click()

        Thread.sleep(2000)

        val rawEta = driver.findElement(eta).text

        return parseEta(rawEta)
    }

    private fun parseEta(rawEta: String) =
        TimestampParser.parseDate("$rawEta 00:00", "eeee, dd-MMM-yyyy HH:mm", "dd.MM.yy")

    override fun scrapesFor(carrier: String) = carrier.trim().equals(carrierCode, true)
}