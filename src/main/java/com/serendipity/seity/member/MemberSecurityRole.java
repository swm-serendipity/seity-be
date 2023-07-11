package com.serendipity.seity.member;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 스프링 시큐리티 관련 role enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
public enum MemberSecurityRole {

    USER("USER", "일반 사용자 권한"),
    ADMIN("ADMIN", "관리자 권한"),
    GUEST("GUEST", "게스트 권한");

    private final String code;
    private final String displayName;
}
