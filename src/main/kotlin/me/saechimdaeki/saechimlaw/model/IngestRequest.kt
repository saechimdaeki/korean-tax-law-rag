package me.saechimdaeki.saechimlaw.model

import me.saechimdaeki.saechimlaw.config.IngestProperties

data class IngestRequest(
    val pdfBytes: ByteArray,
    val title: String? = null,
    val source: String? = null,
    val url: String? = null,
    val tags: List<String>? = null,
    val version: String? = null
) {
    fun withDefaults(props: IngestProperties) = IngestRequest(
        pdfBytes = pdfBytes,
        title = title ?: props.titleDefault,
        source = source ?: props.source,
        url = url ?: props.defaultUrl,
        tags = tags ?: props.tags,
        version = version
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IngestRequest

        if (!pdfBytes.contentEquals(other.pdfBytes)) return false
        if (title != other.title) return false
        if (source != other.source) return false
        if (url != other.url) return false
        if (tags != other.tags) return false
        if (version != other.version) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pdfBytes.contentHashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (source?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        result = 31 * result + (tags?.hashCode() ?: 0)
        result = 31 * result + (version?.hashCode() ?: 0)
        return result
    }
}