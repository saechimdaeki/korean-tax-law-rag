# 소득세 법 질의 응답 시스템

Spring AI를 활용한 한국 세법(소득세법/양도소득세) 전문 질의응답 시스템입니다.

## 🚀 주요 기능

- **PDF 문서 처리**: Apache Tika를 사용한 PDF 텍스트 추출
- **벡터 검색**: Chroma 벡터 스토어를 통한 의미 기반 검색
- **AI 답변 생성**: Ollama LLM을 활용한 컨텍스트 기반 답변
- **문서 청킹**: 효율적인 텍스트 분할 및 오버랩 처리
- **REST API**: 간단한 질의응답 엔드포인트 제공

## 🛠️ 기술 스택

- **Backend**: Spring Boot 3.5.5 + Kotlin
- **AI Framework**: Spring AI 1.0.1
- **LLM**: Ollama (llama3.2:1b)
- **Embedding**: Ollama (nomic-embed-text)
- **Vector Store**: Chroma
- **Document Processing**: Apache Tika



