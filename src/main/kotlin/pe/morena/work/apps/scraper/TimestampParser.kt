package pe.morena.work.apps.scraper

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TimestampParser {
    companion object {
        fun parseDate(raw: String, originalPattern: String, destinationPattern: String): String {
            val date = LocalDateTime.parse(raw, DateTimeFormatter.ofPattern(originalPattern))
            return date.format(DateTimeFormatter.ofPattern(destinationPattern))
        }
    }
}