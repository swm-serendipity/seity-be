package com.serendipity.seity.prompt.dto;

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
    private String question;

    public void init() {
        if (sessionId == null) {
            sessionId = new ObjectId().toString();
        }
    }
}
