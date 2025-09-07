package me.saechimdaeki.saechimlaw.constants

object AppConstants {
    object Ollama {
        const val CHAT_MODEL = "llama3.2:1b"
        const val EMBEDDING_MODEL = "nomic-embed-text"
        const val DEFAULT_TEMPERATURE = 0.1
        const val DEFAULT_NUM_PREDICT = 256
        const val MAX_NUM_PREDICT = 1024
        const val MIN_NUM_PREDICT = 64
    }
    
    object VectorStore {
        const val DEFAULT_TOP_K = 8
        const val DEFAULT_SIMILARITY_THRESHOLD = 0.70
        const val DEFAULT_COLLECTION_NAME = "saechim_taxlaw_collection"
        const val DEFAULT_STATUS = "current"
        const val DEFAULT_SOURCE = "law"
    }
    
    object TextProcessing {
        const val DEFAULT_MAX_CHARS = 800
        const val DEFAULT_OVERLAP = 100
        const val DEFAULT_CHUNK_STEP_RATIO = 0.8
    }
    
    object Http {
        const val DEFAULT_CONNECT_TIMEOUT_SECONDS = 10L
        const val DEFAULT_READ_TIMEOUT_SECONDS = 40L
    }
    
    object DefaultTags {
        val TAX_LAW_TAGS = listOf("소득세법", "양도소득세", "가이드")
    }
    
    object PromptTemplate {
        const val START_DELIMITER = '<'
        const val END_DELIMITER = '>'
    }
}
