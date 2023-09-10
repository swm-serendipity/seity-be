package com.serendipity.seity.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 멘션을 위한 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MentionMemberRequest {

    private String memberId;
}
