package me.saechimdaeki.saechimlaw.exception

/**
 * PDF 처리 관련 예외
 */
class PdfProcessingException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * 벡터 스토어 관련 예외
 */
class VectorStoreException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * 문서 수집 관련 예외
 */
class DocumentIngestionException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
