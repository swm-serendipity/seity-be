package com.serendipity.seity.calling.dto.calling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소명을 전송할 때 사용하는 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CallingSendRequest {

    private String id;
    private String content;
}
