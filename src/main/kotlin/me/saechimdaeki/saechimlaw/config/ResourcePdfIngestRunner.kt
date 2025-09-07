package me.saechimdaeki.saechimlaw.config

import me.saechimdaeki.saechimlaw.model.IngestRequest
import me.saechimdaeki.saechimlaw.service.VectorIngestionService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader

@Configuration
class ResourcePdfIngestRunner(
    private val ingestProperties: IngestProperties,
    private val vectorIngestionService: VectorIngestionService,
    private val resourceLoader: ResourceLoader
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun ingestOnStart(): ApplicationRunner = ApplicationRunner {
        if (!ingestProperties.onStartup) {
            logger.info("앱 시작 시 수집이 비활성화되어 있습니다. (app.ingest.on-startup=false)")
            return@ApplicationRunner
        }

        val resource = resourceLoader.getResource(ingestProperties.pattern)
        if (!resource.exists()) {
            logger.warn("가이드 PDF를 찾을 수 없습니다: {}", ingestProperties.pattern)
            return@ApplicationRunner
        }

        try {
            resource.inputStream.use { inputStream ->
                val title = resource.filename ?: "unknown"
                val ingestRequest = IngestRequest(
                    pdfBytes = inputStream.readAllBytes(),
                    title = title,
                    source = ingestProperties.source,
                    url = ingestProperties.defaultUrl,
                    tags = ingestProperties.tags
                )
                
                val result = vectorIngestionService.upsertPdf(ingestRequest)
                
                logger.info(
                    "수집 완료: {} -> 청크 수: {}, 문서 ID: {}",
                    title,
                    result["chunks"],
                    result["docId"]
                )
            }
        } catch (e: Exception) {
            logger.error("PDF 수집 중 오류 발생: ${e.message}", e)
        }
    }
}