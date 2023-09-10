package com.serendipity.seity.prompt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.config.ChatGptConfig;
import com.serendipity.seity.detection.service.PromptDetectionService;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.dto.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;

import static com.serendipity.seity.common.response.BaseResponseStatus.CHAT_GPT_EXCEPTION;


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
    private final PromptService promptService;
    private final PromptDetectionService promptDetectionService;

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
    public Flux<String> ask(List<ChatGptMessageRequest> messages, List<DetectionRequest> detections,
                            String sessionId, String question, Member member)
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
        AnswerDto answer = new AnswerDto("");

        return client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchangeToFlux(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToFlux(String.class)
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
                                            answer.addAnswer(content);

                                            if (!reasonNode.isNull()) {
                                                finishReason = reasonNode.asText();
                                            }
                                        } catch (NullPointerException ignored) { }

                                        if (finishReason.equals("stop") || finishReason.equals("length")) {

                                            if (question == null) {

                                                // 추가 답변 저장
                                                Prompt prompt = promptService.addExtraAnswer(sessionId, answer.getAnswer());

                                                // 민감정보 탐지 내역 저장
                                                if (detections != null && !detections.isEmpty()) {
                                                    promptDetectionService.createPromptDetection(
                                                            prompt,
                                                            prompt.getQnaList().size() - 1,
                                                            detections,
                                                            member);
                                                }

                                            } else {

                                                // 답변이 끝난 경우 답변을 저장
                                                Prompt prompt = promptService.savePrompt(sessionId, question, answer.getAnswer(), member);

                                                // 민감정보 탐지 내역 저장
                                                if (detections != null && !detections.isEmpty()) {
                                                    promptDetectionService.createPromptDetection(
                                                            prompt,
                                                            prompt.getQnaList().size() - 1,
                                                            detections,
                                                            member);
                                                }
                                            }
                                        }

                                        return responseObjectMapper.writeValueAsString(new QuestionResponse(
                                                sessionId,
                                                content,
                                                finishReason));
                                    } catch (JsonProcessingException e) {
                                        // content 필드가 없는 경우 -> 응답이 끝난 경우
                                        log.error("ask 에서 오류 발생: {}", originalResponse);
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull);
                    } else {
                        // Handle non-2xx responses here
                        log.error("=================gpt 에러 발생=================");

                        for (ChatGptMessageRequest request : messages) {

                            if (request.getRole().equals("user")) {
                                log.error("question: {}", request.getContent());
                            } else {
                                log.error("answer: {}", request.getContent());
                            }
                        }

                        log.error("=================gpt 에러 발생=================");

                        return Flux.error(new BaseException(CHAT_GPT_EXCEPTION));
                    }
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    // Handle WebClientResponseException separately if needed
                    log.error("WebClientResponseException 에러 발생", ex);
                    return Flux.error(new BaseException(CHAT_GPT_EXCEPTION));
                });
    }
}