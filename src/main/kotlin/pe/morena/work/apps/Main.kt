package pe.morena.work.apps

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import pe.morena.work.apps.scraper.Scraper
import java.nio.file.Paths

@Component
@Order(1)
class Main(
    private val fileLogger: FileLogger,
    private val csvReader: CsvReader,
    private val scrapers: List<Scraper>,
) : CommandLineRunner {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun run(vararg args: String?) {
        logger.info("Fetching containers from CSV...")

        val path = Paths.get("inputs", "containers.csv")
        val containersPerCarrier = csvReader.readFile(path).groupBy { it.carrier }

        containersPerCarrier.forEach { e ->
            val carrier = e.key
            val containers = e.value
            val scraper = scrapers.find { it.scrapesFor(carrier) }

            if (scraper != null) {
                containers.forEach { container ->
                    logger.info("Fetching ETAs for $carrier and ${container.number}...")
                    fetchEta(scraper, carrier, container.number)
                }
            }
        }

        logger.info("Finished fetching all containers")
        System.exit(0)
    }

    private fun fetchEta(scraper: Scraper, carrier: String, containerNumber: String) {
        try {
            val eta = scraper.findEta(containerNumber)
            fileLogger.logEta(carrier, containerNumber, eta)
        }
        catch (e: Exception) {
            logger.error("Error while trying to find ETA: ${e.message}", e)
            fileLogger.logEta(carrier, containerNumber, "retry")
        }
    }
}