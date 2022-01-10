package pe.morena.work.apps

import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

@Component
class FileLogger {
    fun logEta(carrier: String, containerNumber: String, eta: String) {
        val file = Paths.get("etas.csv")
        Files.write(file, "$carrier;$containerNumber;$eta\n".toByteArray(),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND
        )
    }
}