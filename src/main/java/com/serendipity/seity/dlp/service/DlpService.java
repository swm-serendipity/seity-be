package com.serendipity.seity.dlp.service;

import com.serendipity.seity.forbiddenword.service.ForbiddenWordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 자체 개발한 dlp 관련 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DlpService {

    private final ForbiddenWordService forbiddenWordService;
    private final String DLP_API_URL = "https://dlp.seity.co.kr/anonymize";

    /**
     * dlp 요청을 보내고, 그에 대한 응답을 받는 메서드입니다.
     * @param question 질문
     * @return dlp 서버로부터의 응답
     */
    public String sendRequest(String question) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("question", question);
        requestBody.put("deny_list", forbiddenWordService.getForbiddenWordByStringList());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                DLP_API_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity.getBody();
    }
}
