package com.serendipity.seity.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

/**
 * 프롬프트 질의를 위한 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {

    private String sessionId;
    private String question;

    public boolean init() {

        if (sessionId == null) {
            sessionId = new ObjectId().toString();
            return true;
        }
        return false;
    }
}
