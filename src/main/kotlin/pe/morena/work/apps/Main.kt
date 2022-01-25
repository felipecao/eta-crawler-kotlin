package pe.morena.work.apps

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import pe.morena.work.apps.scraper.Scraper
import pe.morena.work.apps.slack.SlackConnection
import pe.morena.work.apps.slack.SlackMessage
import java.nio.file.Paths

@Component
@Order(1)
class Main(
    private val slackConnection: SlackConnection,
    private val csvReader: CsvReader,
    private val scrapers: List<Scraper>,
) : CommandLineRunner {

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun run(vararg args: String?) {
        slackConnection.sendMessage(SlackMessage("Starting crawling of ETAs"))

        logger.info("Deleting existing file...")
        OutputFile.delete()

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

        slackConnection.sendMessage(SlackMessage(OutputFile.read()))
        logger.info("Slack notification has been sent, terminating execution")

        System.exit(0)
    }

    private fun fetchEta(scraper: Scraper, carrier: String, containerNumber: String) {
        try {
            val eta = scraper.findEta(containerNumber)
            OutputFile.append(carrier, containerNumber, eta)
        }
        catch (e: Exception) {
            logger.error("Error while trying to find ETA: ${e.message}", e)
            OutputFile.append(carrier, containerNumber, "Not Found")
        }
    }
}