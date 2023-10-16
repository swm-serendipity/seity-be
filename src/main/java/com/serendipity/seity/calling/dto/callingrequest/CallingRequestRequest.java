package com.serendipity.seity.calling.dto.callingrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소명 요청을 하는 데 사용하는 request 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CallingRequestRequest {

    private String promptDetectionId;
}