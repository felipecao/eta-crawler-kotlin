package pe.morena.work.apps.scraper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TimestampParserTest {

    @Test
    fun `timestamp conversion works for CMA`() {
        val raw = "Mon 20 Dec 2021 02:00"
        val originalPattern = "eee dd MMM yyyy HH:mm"
        val destinationPattern = "dd.MM.yy"

        assertThat(TimestampParser.parseDate(raw, originalPattern, destinationPattern)).isEqualTo("20.12.21")
    }

    @Test
    fun `timestamp conversion works for new CMA format works`() {
        val raw = "Monday, 14-Feb-2022 00:00"
        val originalPattern = "eeee, dd-MMM-yyyy HH:mm"
        val destinationPattern = "dd.MM.yy"

        assertThat(TimestampParser.parseDate(raw, originalPattern, destinationPattern)).isEqualTo("14.02.22")
    }

    @Test
    fun `timestamp conversion works for Sealand`() {
        val raw = "14 Jan 2022 00:00"
        val originalPattern = "dd MMM yyyy HH:mm"
        val destinationPattern = "dd.MM.yy"

        assertThat(TimestampParser.parseDate(raw, originalPattern, destinationPattern)).isEqualTo("14.01.22")
    }
}