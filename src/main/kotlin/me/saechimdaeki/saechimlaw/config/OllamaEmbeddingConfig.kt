package me.saechimdaeki.saechimlaw.config

import me.saechimdaeki.saechimlaw.constants.AppConstants
import io.micrometer.observation.ObservationRegistry
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.ollama.OllamaEmbeddingModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.ollama.management.ModelManagementOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OllamaEmbeddingConfig {

    @Bean
    fun embeddingModel(
        ollamaApi: OllamaApi,
        observationRegistry: ObservationRegistry
    ): EmbeddingModel {
        val ollamaOptions = OllamaOptions.builder()
            .model(AppConstants.Ollama.EMBEDDING_MODEL)
            .build()

        val modelManagementOptions = ModelManagementOptions.builder()
            .build()

        return OllamaEmbeddingModel(
            ollamaApi,
            ollamaOptions,
            observationRegistry,
            modelManagementOptions
        )
    }
}