package com.serendipity.seity.calling;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 소명 요청에 대한 상태를 나타내는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
public enum CallingStatus {

    PENDING("PENDING"),                 // 전송되었고, 읽지 않은 상태
    READ("READ"),                       // 전송되었고, 읽은 상태
    CALLING_SENT("CALLING_SENT"),       // 소명을 보낸 상태
    SOLVED("SOLVED");                   // 보낸 소명이 해결된 상태

    private final String value;

}
