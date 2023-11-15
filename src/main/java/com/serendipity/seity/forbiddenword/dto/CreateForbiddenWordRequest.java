package com.serendipity.seity.forbiddenword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 금칙어를 등록할 때 사용하는 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateForbiddenWordRequest {

    private String value;
}
