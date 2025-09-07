package me.saechimdaeki.saechimlaw.config

import me.saechimdaeki.saechimlaw.constants.AppConstants
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.template.st.StTemplateRenderer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatConfig(
    private val builder: ChatClient.Builder, 
    private val vectorStore: VectorStore
) {

    @Bean
    fun chatClient(): ChatClient {
        val ragTemplate = createRagPromptTemplate()
        val questionAnswerAdvisor = createQuestionAnswerAdvisor(ragTemplate)
        val ollamaOptions = createOllamaOptions()
        val systemPrompt = createSystemPrompt()

        return builder
            .defaultSystem(systemPrompt)
            .defaultAdvisors(questionAnswerAdvisor)
            .defaultOptions(ollamaOptions)
            .build()
    }

    private fun createRagPromptTemplate(): PromptTemplate {
        return PromptTemplate.builder()
            .renderer(
                StTemplateRenderer.builder()
                    .startDelimiterToken(AppConstants.PromptTemplate.START_DELIMITER)
                    .endDelimiterToken(AppConstants.PromptTemplate.END_DELIMITER)
                    .build()
            )
            .template(
                """
                다음은 사용자의 질문에 답하기 위한 참고 컨텍스트입니다. 필요할 때만 인용하고, 없으면 추측하지 마세요.

                [컨텍스트]
                ```
                <question_answer_context>
                ```

                [질문]
                <query>

                [응답 규칙]
                - 오직 컨텍스트에 근거해서 간결한 한국어로 답변하세요.
                - 컨텍스트에 근거가 부족하면 한 줄로 "모르겠습니다"라고 답하세요.
                - 근거가 된 위치를 간단히 표기: [출처: {title} #{chunkIndex}]
                """.trimIndent()
            )
            .build()
    }

    private fun createQuestionAnswerAdvisor(template: PromptTemplate): QuestionAnswerAdvisor {
        val searchRequest = SearchRequest.builder()
            .topK(AppConstants.VectorStore.DEFAULT_TOP_K)
            .similarityThreshold(AppConstants.VectorStore.DEFAULT_SIMILARITY_THRESHOLD)
            .filterExpression("status == '${AppConstants.VectorStore.DEFAULT_STATUS}' && source == '${AppConstants.VectorStore.DEFAULT_SOURCE}'")
            .build()

        return QuestionAnswerAdvisor.builder(vectorStore)
            .searchRequest(searchRequest)
            .promptTemplate(template)
            .build()
    }

    private fun createOllamaOptions(): OllamaOptions {
        return OllamaOptions.builder()
            .model(AppConstants.Ollama.CHAT_MODEL)
            .temperature(AppConstants.Ollama.DEFAULT_TEMPERATURE)
            .numPredict(AppConstants.Ollama.DEFAULT_NUM_PREDICT)
            .build()
    }

    private fun createSystemPrompt(): String {
        return """
        당신은 한국 세법(특히 소득세법/양도소득세) 전문 어시스턴트입니다.
        - 컨텍스트 밖의 사실/수치는 절대 추측하지 않습니다.
        - 사용자가 이해하기 쉽게 간결하게 답합니다.
        """.trimIndent()
    }
}