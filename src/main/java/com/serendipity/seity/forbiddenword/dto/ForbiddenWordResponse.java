package com.serendipity.seity.forbiddenword.dto;

import com.serendipity.seity.forbiddenword.ForbiddenWord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 금칙어를 나타내는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ForbiddenWordResponse {

    private String id;
    private String value;

    public static ForbiddenWordResponse of(ForbiddenWord word) {

        return new ForbiddenWordResponse(word.getId(), word.getValue());
    }
}
