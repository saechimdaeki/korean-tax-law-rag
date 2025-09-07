package me.saechimdaeki.saechimlaw.service

import me.saechimdaeki.saechimlaw.config.IngestProperties
import me.saechimdaeki.saechimlaw.constants.AppConstants
import me.saechimdaeki.saechimlaw.exception.DocumentIngestionException
import me.saechimdaeki.saechimlaw.exception.PdfProcessingException
import me.saechimdaeki.saechimlaw.exception.VectorStoreException
import me.saechimdaeki.saechimlaw.model.IngestRequest
import me.saechimdaeki.saechimlaw.model.LawChunkMeta
import me.saechimdaeki.saechimlaw.model.toMap
import me.saechimdaeki.saechimlaw.util.TextUtils
import org.apache.tika.Tika
import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.time.Instant

@Service
class VectorIngestionService(
    private val vectorStore: VectorStore,
    private val tika: Tika,
    private val ingestProperties: IngestProperties
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun upsertPdf(request: IngestRequest): Map<String, Any> {
        val processedRequest = request.withDefaults(ingestProperties)
        
        return try {
            val rawContent = extractTextFromPdf(processedRequest.pdfBytes)
            val normalizedContent = TextUtils.normalize(rawContent)
            val documentId = generateDocumentId(processedRequest, normalizedContent)
            val chunks = TextUtils.chunk(normalizedContent, ingestProperties.maxChars, ingestProperties.overlap)
            val documents = createDocuments(chunks, processedRequest, documentId)
            
            upsertDocuments(documents)
            
            val result = mapOf(
                "docId" to documentId,
                "chunks" to chunks.size,
                "ids" to documents.map { it.id }
            )
            
            logger.info(
                "PDF 업서트 완료: '{}' -> 청크 수: {}, 문서 ID: {}", 
                processedRequest.title, 
                chunks.size, 
                documentId
            )
            
            result
        } catch (e: PdfProcessingException) {
            logger.error("PDF 처리 오류: ${e.message}", e)
            throw e
        } catch (e: VectorStoreException) {
            logger.error("벡터 스토어 오류: ${e.message}", e)
            throw e
        } catch (e: Exception) {
            logger.error("PDF 업서트 중 예상치 못한 오류 발생: ${e.message}", e)
            throw DocumentIngestionException("PDF 처리 중 오류가 발생했습니다: ${e.message}", e)
        }
    }
    
    private fun extractTextFromPdf(pdfBytes: ByteArray): String {
        return try {
            ByteArrayInputStream(pdfBytes).use { inputStream ->
                tika.parseToString(inputStream)
            }
        } catch (e: Exception) {
            throw PdfProcessingException("PDF에서 텍스트 추출 중 오류가 발생했습니다: ${e.message}", e)
        }
    }
    
    private fun generateDocumentId(request: IngestRequest, content: String): String {
        return TextUtils.sha256("${request.title}|${request.source}|${request.url}|$content")
    }
    
    private fun createDocuments(
        chunks: List<String>, 
        request: IngestRequest, 
        documentId: String
    ): List<Document> {
        val currentTime = Instant.now()
        
        return chunks.mapIndexed { index, chunk ->
            val chunkId = "$documentId:$index"
            val metadata = LawChunkMeta(
                id = chunkId,
                docId = documentId,
                chunkIndex = index,
                title = request.title!!,
                source = request.source!!,
                url = request.url,
                tags = request.tags!!,
                ingestedAt = currentTime,
                version = request.version,
                status = AppConstants.VectorStore.DEFAULT_STATUS
            )
            Document(chunkId, chunk, metadata.toMap())
        }
    }
    
    private fun upsertDocuments(documents: List<Document>) {
        try {
            val documentIds = documents.map { it.id }
            
            // 기존 문서 삭제 후 새 문서 추가
            vectorStore.delete(documentIds)
            vectorStore.add(documents)
        } catch (e: Exception) {
            throw VectorStoreException("벡터 스토어에 문서 저장 중 오류가 발생했습니다: ${e.message}", e)
        }
    }
}