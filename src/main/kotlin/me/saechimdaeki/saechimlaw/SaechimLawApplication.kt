package me.saechimdaeki.saechimlaw

import me.saechimdaeki.saechimlaw.config.IngestProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(IngestProperties::class)
class SaechimLawApplication

fun main(args: Array<String>) {
    runApplication<SaechimLawApplication>(*args)
}
