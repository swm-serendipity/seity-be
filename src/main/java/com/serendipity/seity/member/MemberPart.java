package com.serendipity.seity.member;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;

/**
 * member의 부서를 나타내는 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
public enum MemberPart {

    FRONT_END("FRONT_END"),
    BACK_END("BACK_END"),
    AI("AI");

    private final String value;

    MemberPart(String value) {
        this.value = value;
    }

    public static MemberPart getPart(String part) throws BaseException {

        for (MemberPart value: MemberPart.values()) {

            if (value.value.equals(part)) {
                return value;
            }
        }

        throw new BaseException(BaseResponseStatus.INVALID_MEMBER_PART_EXCEPTION);
    }
}
