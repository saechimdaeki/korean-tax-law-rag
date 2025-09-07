package me.saechimdaeki.saechimlaw.config

import me.saechimdaeki.saechimlaw.constants.AppConstants
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.ingest")
data class IngestProperties(
    var onStartup: Boolean = true,
    var pattern: String = "classpath:laws/income_tax/income_tax.pdf",
    var source: String = AppConstants.VectorStore.DEFAULT_SOURCE,
    var tags: List<String> = AppConstants.DefaultTags.TAX_LAW_TAGS,
    var titleDefault: String = "소득세법_가이드",
    var maxChars: Int = AppConstants.TextProcessing.DEFAULT_MAX_CHARS,
    var overlap: Int = AppConstants.TextProcessing.DEFAULT_OVERLAP,
    var defaultUrl: String = ""
)