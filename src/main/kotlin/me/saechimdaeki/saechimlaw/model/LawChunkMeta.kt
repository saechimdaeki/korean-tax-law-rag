package me.saechimdaeki.saechimlaw.model

import java.time.Instant

data class LawChunkMeta(
    val id: String,
    val docId: String,
    val chunkIndex: Int,
    val title: String,
    val source: String,
    val url: String?,
    val tags: List<String>,
    val ingestedAt: Instant,
    val version: String? = null,
    val status: String = "current"
)

fun LawChunkMeta.toMap(): Map<String, Any> = buildMap {
    put("id", id)
    put("docId", docId)
    put("chunkIndex", chunkIndex)
    put("title", title)
    put("source", source)
    if (!url.isNullOrBlank()) put("url", url)
    put("tags", tags)
    put("ingestedAt", ingestedAt.toString())
    put("status", status)
    version?.let { put("version", it) }
}