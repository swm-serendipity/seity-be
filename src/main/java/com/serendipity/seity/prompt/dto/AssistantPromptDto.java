package com.serendipity.seity.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * assistant prompt를 생성할 떄 사용하는 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AssistantPromptDto {

    private List<ChatGptMessageRequest> assistantPrompt;
    private String chatModel;
}
