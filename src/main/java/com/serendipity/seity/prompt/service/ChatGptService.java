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
import org.json.JSONObject;
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

    private final String AUTHORIZATION_HEADER = "Authorization";
    private final String BEARER_PREFIX = "Bearer ";


    private final PromptService promptService;
    private WebClient client;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    @PostConstruct
    public void init() {
        client = WebClient.builder()
                .baseUrl(openAiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + openAiKey)
                .build();
    }

    /**
     * 질문 메서드입니다.
     * @param id 해당 document의 id
     * @param question 질문
     * @return 답변에 대한 flux 객체
     * @throws JsonProcessingException json 파싱에서 예외가 발생한 경우
     */
    public Flux<String> ask(String id, String question) throws JsonProcessingException {

        List<ChatGptMessageRequest> messages = promptService.getAllPromptById(id);
        messages.add(new ChatGptMessageRequest("user", question));

        ChatGptRequest chatGptRequest = new ChatGptRequest(
                ChatGptConfig.CHAT_MODEL,
                ChatGptConfig.MAX_TOKEN,
                ChatGptConfig.TEMPERATURE,
                ChatGptConfig.STREAM,
                messages
        );

        String requestValue = objectMapper.writeValueAsString(chatGptRequest);

        return client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchangeToFlux(response -> response.bodyToFlux(String.class)
                        .mapNotNull(originalResponse -> {

                            ObjectMapper objectMapper2 = new ObjectMapper();
                            // 정상 응답
                            try {
                                if (originalResponse.equals("[DONE]")) {
                                    return objectMapper2.writeValueAsString(originalResponse);
                                }
                                JsonNode jsonNode = objectMapper2.readTree(originalResponse);
                                String content = "";
                                try {
                                    JsonNode contentNode = jsonNode.path("choices").get(0).path("delta").path("content");
                                    content = contentNode.isMissingNode() ? "" : contentNode.asText();
                                } catch (NullPointerException ignored) { }

                                return objectMapper2.writeValueAsString(new QuestionResponse(id, content));
                            } catch (JsonProcessingException e) {
                                // content 필드가 없는 경우 -> 응답이 끝난 경우
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull));
    }

    /**
     * 생성된 답변을 1개의 문자열로 반환하는 메서드입니다.
     * @param answerFlux 답변 flux 객체
     * @throws JsonProcessingException JSON 파싱에서 예외가 발생한 경우
     */
    public StringBuilder getAnswerStringFromFlux(Flux<String> answerFlux) throws JsonProcessingException {

        List<String> answers = answerFlux.collectList().block(); // Flux<String>을 List<String>으로 수집

        StringBuilder combinedAnswer = new StringBuilder();
        assert answers != null;

        for (String answer : answers) {
            combinedAnswer.append(extractValue(answer, "answer"));
        }

        return combinedAnswer;
    }

    private static String extractValue(String jsonString, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (jsonObject.has(key)) {
                return jsonObject.getString(key);
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }
}