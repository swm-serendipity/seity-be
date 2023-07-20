package com.serendipity.seity.prompt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프롬프트에서의 각 질문을 나타내는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Qna {

    private String question;
    private String answer;
}
