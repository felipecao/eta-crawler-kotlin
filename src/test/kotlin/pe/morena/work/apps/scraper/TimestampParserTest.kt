package pe.morena.work.apps.scraper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TimestampParserTest {

    @Test
    fun `timestamp conversion works properly`() {
        val raw = "Mon 20 Dec 2021 02:00"
        val originalPattern = "eee dd MMM yyyy HH:mm"
        val destinationPattern = "dd.MM.yy"

        assertThat(TimestampParser.parseDate(raw, originalPattern, destinationPattern)).isEqualTo("20.12.21")
    }
}