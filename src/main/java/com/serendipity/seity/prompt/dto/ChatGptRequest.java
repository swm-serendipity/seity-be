package com.serendipity.seity.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ChatGPT API의 request body에 들어갈 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptRequest {

    private String model;
    private Integer maxTokens;
    private Double temperature;
    private boolean stream;
    private List<ChatGptMessageRequest> messages;
}
