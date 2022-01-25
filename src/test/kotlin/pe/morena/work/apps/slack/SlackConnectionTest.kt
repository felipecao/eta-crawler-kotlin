package pe.morena.work.apps.slack

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import pe.morena.work.apps.config.SlackConfig

class SlackConnectionTest {

    @Test
    fun `send Slack message`() {
        val config = SlackConfig(host = "hooks.slack.com")
        val conn = SlackConnection(config)

        conn.sendMessage(SlackMessage(text = "oi, morena!"))
    }
}