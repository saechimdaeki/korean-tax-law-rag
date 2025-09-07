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

## 🚀 실행 방법

```bash
# 의존성 서비스 시작
docker-compose up -d

# 애플리케이션 실행
./gradlew bootRun
```

## 📖 API 사용법

```bash
# 질의응답 API
curl "http://localhost:8080/chat?q=소득세 계산 방법은?"

# 응답 예시
{
  "answer": "소득세는 종합소득금액에서 기본공제, 연금보험료공제 등을 차감한 과세표준에 세율을 적용하여 계산합니다. [출처: 소득세법_가이드 #1]"
}
```

## 📁 프로젝트 구조

```
src/main/kotlin/me/saechimdaeki/saechimlaw/
├── config/                    # 설정 클래스들
│   ├── ChatConfig.kt          # 채팅 클라이언트 설정
│   ├── HttpClientConfig.kt    # HTTP 클라이언트 설정
│   ├── IngestProperties.kt    # 문서 수집 설정
│   └── ...
├── constants/
│   └── AppConstants.kt        # 애플리케이션 상수
├── controller/
│   └── ChatController.kt      # REST API 컨트롤러
├── exception/
│   ├── CustomExceptions.kt    # 커스텀 예외 클래스
│   └── GlobalExceptionHandler.kt # 전역 예외 처리
├── model/                     # 데이터 모델들
├── service/
│   └── VectorIngestionService.kt # 벡터 수집 서비스
└── util/
    └── TextUtils.kt           # 텍스트 처리 유틸리티
```
