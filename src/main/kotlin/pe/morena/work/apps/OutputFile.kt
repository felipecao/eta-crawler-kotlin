package pe.morena.work.apps

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.io.path.deleteIfExists

class OutputFile {

    companion object {

        private val fileName = "etas.csv"

        fun delete() {
            Paths.get(fileName).deleteIfExists()
        }

        fun append(carrier: String, containerNumber: String, eta: String) {
            val file = Paths.get(fileName)
            Files.write(file, "$carrier;$containerNumber;$eta\n".toByteArray(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            )
        }

        fun read(): String {
            return Files.readAllLines(Paths.get(fileName)).joinToString("\n")
        }
    }

    fun logEta(carrier: String, containerNumber: String, eta: String) {
        val file = Paths.get("etas.csv")
        Files.write(file, "$carrier;$containerNumber;$eta\n".toByteArray(),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND
        )
    }
}