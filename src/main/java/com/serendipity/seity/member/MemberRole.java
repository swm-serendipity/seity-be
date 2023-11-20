package com.serendipity.seity.member;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.serendipity.seity.common.response.BaseResponseStatus.INVALID_MEMBER_ROLE_EXCEPTION;


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

    public static MemberRole of(String value) throws BaseException {

        for (MemberRole role: MemberRole.values()) {
            if (role.code.equals(value)) {
                return role;
            }
        }

        throw new BaseException(INVALID_MEMBER_ROLE_EXCEPTION);
    }

    public static MemberRole ofForUpdate(String value) throws BaseException {

        for (MemberRole role: MemberRole.values()) {
            if (role.code.equals(value)) {
                return role;
            }
        }

        return null;
    }
}
