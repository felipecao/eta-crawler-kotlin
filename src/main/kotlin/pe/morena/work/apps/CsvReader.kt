package pe.morena.work.apps

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class CsvReader {
    fun readFile(path: Path): List<Container> = csvReader { delimiter = ';' }
        .readAllWithHeader(path.toFile())
        .distinct()
        .map { csv -> Container(csv.get("Carrier")!!, csv.get("Container Number")!!) }
}

data class Container(val carrier: String, val number: String)