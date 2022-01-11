package pe.morena.work.apps.scraper

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class SealandScraper (private val driver: ChromeDriver) : Scraper {

    companion object {
        private val carrierCode = "SEALAND"
        private val consentCookie = "CookieInformationConsent"
        private val regionCookie = "regionSelected"
        private val allowAll = By.cssSelector("button[aria-label='Allow all']")
        private val input = By.cssSelector("input[id^=tracking-number]")
        private val searchButton = By.cssSelector("button[type=submit]")
        private val map = By.className("regional-selection__map__options")
        private val radionButtonRegion = "radio-button__display"
        private val btnConfirmRegion = By.id("confirmRegion")
        private val searchSummary = By.className("search-summary")
        private val eta = By.cssSelector("dd[data-test=container-eta-value]")
    }

    @Retryable(
        include = [NoSuchElementException::class],
        backoff = Backoff(delay = 1000),
        maxAttempts = 3
    )
    override fun findEta(containerNumber: String): String {
        val wait = WebDriverWait(driver, 10)

        driver.navigate().to("https://www.sealandmaersk.com/")

        if (driver.manage().getCookieNamed(consentCookie) == null) {
            wait.until(visibilityOfElementLocated(allowAll))
            driver.findElement(allowAll).click()
        }

        driver.findElement(input).sendKeys(containerNumber)
        driver.findElement(searchButton).click()

        if (driver.manage().getCookieNamed(regionCookie) == null) {
            wait.until(visibilityOfElementLocated(map))
            wait.until(visibilityOf(driver.findElement(btnConfirmRegion)))

            driver.findElementsByClassName(radionButtonRegion).find { it.text.trim().startsWith("Europe") }?.click()

            Thread.sleep(2000)
            driver.findElement(btnConfirmRegion).click();
        }

        wait.until(visibilityOfElementLocated(searchSummary))

        return parseEta(driver.findElement(eta).text)
    }

    private fun parseEta(rawEta: String) =
        TimestampParser.parseDate("$rawEta 00:00", "dd MMM yyyy HH:mm", "dd.MM.yy")

    override fun scrapesFor(carrier: String) = carrier.trim().equals(carrierCode, true)
}