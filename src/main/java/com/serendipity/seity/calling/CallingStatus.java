package com.serendipity.seity.calling;

/**
 * 소명 요청에 대한 상태를 나타내는 클래스입니다.
 *
 * @author Min Ho CHO
 */
public enum CallingStatus {

    PENDING,            // 전송되었고, 읽지 않은 상태
    READ,               // 전송되었고, 읽은 상태
    CALLING_SENT,       // 소명을 보낸 상태
    SOLVED              // 보낸 소명이 해결된 상태
}
