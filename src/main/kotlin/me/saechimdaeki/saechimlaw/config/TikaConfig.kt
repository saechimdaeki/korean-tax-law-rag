package me.saechimdaeki.saechimlaw.config

import org.apache.tika.Tika
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TikaConfig {
    @Bean
    fun tika(): Tika = Tika()
}