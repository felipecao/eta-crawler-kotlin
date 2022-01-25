package pe.morena.work.apps.config

import org.springframework.context.annotation.Configuration

@Configuration
data class SlackConfig (val protocol: String = "https", val host: String = "hooks.slack.com", val port: Int = 443)