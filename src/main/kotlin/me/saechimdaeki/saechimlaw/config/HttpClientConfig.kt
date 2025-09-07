package me.saechimdaeki.saechimlaw.config

import me.saechimdaeki.saechimlaw.constants.AppConstants
import org.springframework.boot.web.client.RestClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import java.time.Duration

@Configuration
class HttpClientConfig {

    @Bean
    fun restClientCustomizer(): RestClientCustomizer = RestClientCustomizer { builder ->
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(Duration.ofSeconds(AppConstants.Http.DEFAULT_CONNECT_TIMEOUT_SECONDS))
            setReadTimeout(Duration.ofSeconds(AppConstants.Http.DEFAULT_READ_TIMEOUT_SECONDS))
        }
        builder.requestFactory(requestFactory)
    }
}