package com.serendipity.seity.member;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;

/**
 * member의 직무를 표현하는 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
public enum MemberRole {

    GENERAL("GENERAL"),
    ADMIN("ADMIN");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

    public static MemberRole getRole(String role) throws BaseException {

        for (MemberRole value : MemberRole.values()) {

            if (value.value.equals(role)) {
                return value;
            }
        }

        throw new BaseException(BaseResponseStatus.INVALID_MEMBER_ROLE_EXCEPTION);
    }

}
