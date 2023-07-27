package com.serendipity.seity.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * query parameter를 적지 않은 오류 상황 발생 시, 해당 parameter를 표현하는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissingQueryParameterResponse {

    private String name;
    private String type;
}
