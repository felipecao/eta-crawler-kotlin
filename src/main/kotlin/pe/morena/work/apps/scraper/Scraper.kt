package pe.morena.work.apps.scraper

interface Scraper {
    fun findEta(containerNumber: String) : String
    fun scrapesFor(carrier: String): Boolean
}