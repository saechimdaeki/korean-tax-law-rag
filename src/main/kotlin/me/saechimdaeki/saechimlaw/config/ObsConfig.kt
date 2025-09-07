package me.saechimdaeki.saechimlaw.config

import io.micrometer.observation.ObservationRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObsConfig {
    @Bean
    fun observationRegistry(): ObservationRegistry = ObservationRegistry.create()
}