package pe.morena.work.apps.slack

data class SlackMessage (val text: String) {
    fun asMap(): Map<String, String> {
        return mapOf(Pair("text", text))
    }
}