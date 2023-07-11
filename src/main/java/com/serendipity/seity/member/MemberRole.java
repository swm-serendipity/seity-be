package com.serendipity.seity.member;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * member role enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
public enum MemberRole {

    USER("USER", "일반 사용자 권한"),
    ADMIN("ADMIN", "보안 담당자 권한");

    private final String code;
    private final String displayName;
}
