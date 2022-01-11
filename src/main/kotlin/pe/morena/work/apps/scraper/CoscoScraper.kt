package pe.morena.work.apps.scraper

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
class CoscoScraper (private val driver: ChromeDriver) : Scraper {

    private val wait = WebDriverWait(driver, 10)

    companion object {
        private val carrierCode = "COSCO"
        private val cookiePolicy = "cookiePolicy"
        private val btnAllowCookies = By.cssSelector("button[class='ivu-btn ivu-btn-primary ivu-btn-large']")
        private val comboBox = By.cssSelector("span[class='ivu-select-selected-value']")
        private val trackingOptions = By.cssSelector("ul[class='ivu-select-dropdown-list']")
        private val input = By.cssSelector("input[class='ivu-input']")
        private val searchButton = By.cssSelector("button[class='cargoTrackSearchBtn ivu-btn ivu-btn-primary']")
        private val etaLabel = By.cssSelector("p[class='label']")
        private val eta = By.cssSelector("p[class='date']")
        private val etaHasLoaded = ExpectedCondition {
            (it != null) && (it.findElement(eta) != null) && (it.findElement(eta).text != null) &&
                !(it.findElement(eta).text.startsWith("Loading", true))
        }
    }

    @Retryable(
        include = [NoSuchElementException::class],
        backoff = Backoff(delay = 1000),
        maxAttempts = 3
    )
    override fun findEta(containerNumber: String): String {
        driver.navigate().to("https://elines.coscoshipping.com/ebusiness/cargotracking")

        if (driver.manage().getCookieNamed(cookiePolicy) == null) {
            wait.until(visibilityOfElementLocated(btnAllowCookies))
            driver.findElement(btnAllowCookies).click()
        }

        driver.findElement(comboBox).click()

        (driver.findElement(trackingOptions) as RemoteWebElement)
            .findElementsByTagName("li")[2]
            .click()

        driver.findElement(input).clear()
        driver.findElement(input).sendKeys(containerNumber)
        driver.findElement(searchButton).click()

        wait.until(visibilityOfElementLocated(etaLabel))
        wait.until(etaHasLoaded)

        return parseEta(driver.findElement(eta).text)
    }

    private fun parseEta(rawEta: String) =
        TimestampParser.parseDate(rawEta, "yyyy-MM-dd HH:mm", "dd.MM.yy")

    override fun scrapesFor(carrier: String) = carrier.trim().equals(carrierCode, true)
}