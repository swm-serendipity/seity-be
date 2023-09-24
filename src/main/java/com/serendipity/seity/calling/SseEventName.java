package com.serendipity.seity.calling;

import lombok.Getter;

/**
 * SSE의 event 목록을 정의한 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
public enum SseEventName {

    CALLING_REQUEST("CALLING_REQUEST"),
    CALLING_SENT("CALLING_SENT")
    ;

    private final String value;

    SseEventName(String value) {
        this.value = value;
    }
}
