package com.serendipity.seity.calling;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * SSE의 key rule을 정의한 클래스입니다.
 *
 * @author Min Ho CHO
 */
@RequiredArgsConstructor
@EqualsAndHashCode
public class SseRepositoryKeyRule {

    private static final String UNDER_SCORE = "_";

    private final String memberId;
    private final SseEventName sseEventName;
    private final LocalDateTime createdAt;

    /**
     * SSEInMemoryRepository에서 사용될 특정 user에 대한 특정 브라우저,특정 SSEEventName에 대한 SSEEmitter를 찾기 위한 key를
     * 생성하는 메서드입니다.
     * @return 생성된 key
     */
    public String toCompleteKeyWhichSpecifyOnlyOneValue() {

        String createdAtString = createdAt == null ? "" : createdAt.toString();
        return memberId + UNDER_SCORE + sseEventName.getValue() + UNDER_SCORE + createdAtString;
    }
}
