package me.saechimdaeki.saechimlaw.model

/**
 * 에러 응답 모델
 */
data class ErrorResponse(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)
