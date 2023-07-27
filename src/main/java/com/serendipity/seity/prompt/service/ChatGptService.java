package com.serendipity.seity.prompt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.serendipity.seity.config.ChatGptConfig;
import com.serendipity.seity.prompt.dto.ChatGptRequest;
import com.serendipity.seity.prompt.dto.ChatGptMessageRequest;
import com.serendipity.seity.prompt.dto.QuestionResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;


/**
 * ChatGPT 관련 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ChatGptService {

    @Value("${openai.url}")
    private String openAiUrl;

    @Value("${openai.key}")
    private String openAiKey;

    private WebClient client;

    private final ObjectMapper gptObjectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @PostConstruct
    public void init() {
        client = WebClient.builder()
                .baseUrl(openAiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + openAiKey)
                .build();
    }

    /**
     * 질문 메서드입니다.
     * @param messages 이전 질문
     * @param question 질문
     * @return 답변에 대한 flux 객체
     * @throws JsonProcessingException json 파싱에서 예외가 발생한 경우
     */
    public Flux<String> ask(List<ChatGptMessageRequest> messages, String sessionId, String question)
            throws JsonProcessingException {

        if (question != null) {
            messages.add(new ChatGptMessageRequest(ChatGptConfig.USER_ROLE, question));
        }

        ChatGptRequest chatGptRequest = new ChatGptRequest(
                ChatGptConfig.CHAT_MODEL,
                ChatGptConfig.MAX_TOKEN,
                ChatGptConfig.TEMPERATURE,
                ChatGptConfig.STREAM,
                messages
        );

        String requestValue = gptObjectMapper.writeValueAsString(chatGptRequest);

        return client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchangeToFlux(response -> response.bodyToFlux(String.class)
                        .mapNotNull(originalResponse -> {

                            ObjectMapper responseObjectMapper = new ObjectMapper();
                            // 정상 응답
                            try {
                                if (originalResponse.equals(ChatGptConfig.DONE_MESSAGE)) {
                                    return responseObjectMapper.writeValueAsString(originalResponse);
                                }

                                JsonNode jsonNode = responseObjectMapper.readTree(originalResponse);
                                String content = "";
                                String finishReason = ChatGptConfig.PROCEEDING_RESPONSE;
                                try {
                                    JsonNode contentNode = jsonNode.path("choices")
                                            .get(0).path("delta").path("content");
                                    JsonNode reasonNode = jsonNode.path("choices")
                                            .get(0).path("finish_reason");
                                    content = contentNode.isMissingNode() ? "" : contentNode.asText();

                                    if (!reasonNode.isNull()) {
                                        finishReason = reasonNode.asText();
                                    }
                                } catch (NullPointerException ignored) { }

                                return responseObjectMapper.writeValueAsString(new QuestionResponse(sessionId,
                                        content, finishReason));
                            } catch (JsonProcessingException e) {
                                // content 필드가 없는 경우 -> 응답이 끝난 경우
                                log.error("ask 에서 오류 발생: {}", originalResponse);
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull));
    }
}