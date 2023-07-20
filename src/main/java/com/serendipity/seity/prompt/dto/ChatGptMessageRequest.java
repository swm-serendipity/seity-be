package com.serendipity.seity.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 실제 ChatGPT API의 request의 messages에 사용되는 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatGptMessageRequest {

    private String role;
    private String content;
}
