package com.serendipity.seity.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ChatGPT API로부터 받은 답변을 저장하는 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private String answer;

    public void addAnswer(String answer) {
        this.answer += answer;
    }
}
