package com.serendipity.seity.prompt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

/**
 * 프롬프트 질의에 사용되는 request 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromptAskRequest {

    private String sessionId;

    @NotBlank(message = "질문을 입력해주세요.")
    private String question;

    public void init() {
        if (sessionId == null) {
            sessionId = new ObjectId().toString();
        }
    }
}
