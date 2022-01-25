package pe.morena.work.apps.slack

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pe.morena.work.apps.config.SlackConfig
import khttp.responses.Response

@Component
class SlackConnection(@Autowired private val config: SlackConfig) {
    fun sendMessage(msg: SlackMessage): Response {
        val url = "${config.protocol}://${config.host}:${config.port}/services/T0C9MFUAW/B0308N7PW12/WZYtJM7ivNGoV4WkdonyKvmI"
        return khttp.post(url,
            headers = mapOf(
                Pair("Content-Type", "application/json")
            ),
            json = msg.asMap()
        )
    }
}