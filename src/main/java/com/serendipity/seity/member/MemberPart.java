package com.serendipity.seity.member;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;
import lombok.Getter;

/**
 * member의 부서를 나타내는 enum 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
public enum MemberPart {

    FRONT_END("Front-end", "FRONT_END"),
    BACK_END("Back-end", "BACK_END"),
    AI("AI", "AI");

    private final String name;
    private final String value;

    MemberPart(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static MemberPart of(String part) throws BaseException {

        for (MemberPart value: MemberPart.values()) {

            if (value.value.equals(part)) {
                return value;
            }
        }

        throw new BaseException(BaseResponseStatus.INVALID_MEMBER_PART_EXCEPTION);
    }

    public static MemberPart ofForUpdate(String part) throws BaseException {

        for (MemberPart value: MemberPart.values()) {

            if (value.value.equals(part)) {
                return value;
            }
        }

        return null;
    }
}
