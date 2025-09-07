package me.saechimdaeki.saechimlaw.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object TextUtils {

    fun normalize(text: String?): String {
        return text?.let { input ->
            input
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace("\t", " ")
                .replace("\u00A0", " ") // Non-breaking space
                .replace(Regex(" +"), " ")
                .trim()
        } ?: ""
    }

    fun sha256(input: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashBytes = messageDigest.digest(input.toByteArray(StandardCharsets.UTF_8))
        
        return buildString(hashBytes.size * 2) {
            hashBytes.forEach { byte ->
                append(String.format("%02x", byte))
            }
        }
    }

    fun chunk(textRaw: String, maxChars: Int, overlap: Int): List<String> {
        val normalizedText = normalize(textRaw)
        val paragraphs = normalizedText.split(Regex("\\n\\s*\\n"))
        val chunks = mutableListOf<String>()
        val buffer = StringBuilder()

        for (paragraph in paragraphs) {
            if (buffer.isNotEmpty() && buffer.length + paragraph.length + 1 > maxChars) {
                chunks.add(buffer.toString().trim())
                val tail = buffer.substring((buffer.length - overlap).coerceAtLeast(0))
                buffer.setLength(0)
                buffer.append(tail)
            }
            buffer.append(paragraph).append("\n\n")
        }
        
        if (buffer.isNotEmpty()) {
            chunks.add(buffer.toString().trim())
        }

        val finalChunks = mutableListOf<String>()
        for (chunk in chunks) {
            if (chunk.length <= maxChars) {
                finalChunks.add(chunk)
            } else {
                splitLargeChunk(chunk, maxChars, overlap, finalChunks)
            }
        }
        
        return finalChunks.filter { it.isNotBlank() }
    }
    
    private fun splitLargeChunk(
        chunk: String, 
        maxChars: Int, 
        overlap: Int, 
        result: MutableList<String>
    ) {
        val stepSize = (maxChars - overlap).coerceAtLeast(1)
        var startIndex = 0
        
        while (startIndex < chunk.length) {
            val endIndex = (startIndex + stepSize).coerceAtMost(chunk.length)
            result.add(chunk.substring(startIndex, endIndex))
            startIndex += stepSize
        }
    }
}