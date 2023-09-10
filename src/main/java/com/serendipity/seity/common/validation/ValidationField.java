package com.serendipity.seity.common.validation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 1개의 validation 오류에 대한 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NotNull
public class ValidationField {

    private String fieldName;
    private String message;
}
