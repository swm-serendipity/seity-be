package com.serendipity.seity.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * validation 오류가 발생했을 때
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {

    private List<ValidationField> validationErrorList = new ArrayList<>();
}
