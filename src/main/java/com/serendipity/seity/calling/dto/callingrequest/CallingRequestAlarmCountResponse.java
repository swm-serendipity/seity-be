package com.serendipity.seity.calling.dto.callingrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소명 요청 알림의 안 읽은 개수를 표현하는 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CallingRequestAlarmCountResponse {

    int count;
}
