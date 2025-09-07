package me.saechimdaeki.saechimlaw.exception

import me.saechimdaeki.saechimlaw.model.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    
    private val logger = LoggerFactory.getLogger(javaClass)
    
    @ExceptionHandler(PdfProcessingException::class)
    fun handlePdfProcessingException(e: PdfProcessingException): ResponseEntity<ErrorResponse> {
        logger.error("PDF 처리 오류: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse("PDF 처리 중 오류가 발생했습니다: ${e.message}"))
    }
    
    @ExceptionHandler(VectorStoreException::class)
    fun handleVectorStoreException(e: VectorStoreException): ResponseEntity<ErrorResponse> {
        logger.error("벡터 스토어 오류: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("벡터 스토어 처리 중 오류가 발생했습니다: ${e.message}"))
    }
    
    @ExceptionHandler(DocumentIngestionException::class)
    fun handleDocumentIngestionException(e: DocumentIngestionException): ResponseEntity<ErrorResponse> {
        logger.error("문서 수집 오류: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("문서 수집 중 오류가 발생했습니다: ${e.message}"))
    }
    
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.warn("잘못된 인수: ${e.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse("잘못된 요청입니다: ${e.message}"))
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error("예상치 못한 오류 발생: ${e.message}", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("서버 내부 오류가 발생했습니다."))
    }
}
