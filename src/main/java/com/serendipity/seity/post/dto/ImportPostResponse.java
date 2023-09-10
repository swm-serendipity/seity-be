package com.serendipity.seity.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 공유된 프롬프트 세션을 자신의 세션으로 추가할 때 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImportPostResponse {

    private String sessionId;
}
