package me.saechimdaeki.saechimlaw.controller

import me.saechimdaeki.saechimlaw.constants.AppConstants
import me.saechimdaeki.saechimlaw.model.AnswerResponse
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val chatClient: ChatClient
) {
    
    @GetMapping
    fun ask(@RequestParam("q") question: String): ResponseEntity<AnswerResponse> {
        require(question.isNotBlank()) { "질문은 비어있을 수 없습니다." }
        
        val perRequestOptions = createPerRequestOptions()
        val answer = chatClient.prompt()
            .options(perRequestOptions)
            .user(question)
            .call()
            .content()?:"죄송합니다 응답을 생성할 수 없습니다"

        return ResponseEntity.ok(AnswerResponse(answer))
    }
    
    private fun createPerRequestOptions(): OllamaOptions {
        return OllamaOptions.builder()
            .numPredict(
                AppConstants.Ollama.DEFAULT_NUM_PREDICT
                    .coerceIn(AppConstants.Ollama.MIN_NUM_PREDICT, AppConstants.Ollama.MAX_NUM_PREDICT)
            )
            .build()
    }
}