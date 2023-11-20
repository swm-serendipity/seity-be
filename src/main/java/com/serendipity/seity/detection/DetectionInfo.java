package com.serendipity.seity.detection;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 어떤 민감정보가 탐지되었는지를 지칭하는 enum 클래스입니다.
 */
@Getter
@AllArgsConstructor
public enum DetectionInfo {

    PRIVACY("PRIVACY"),
    CORPORATE_IMPORTANT_INFORMATION("CORPORATE_IMPORTANT_INFORMATION"),
    DENY_LIST("DENY_LIST");

    private final String value;

    public static DetectionInfo of(String value) {

        for (DetectionInfo info : DetectionInfo.values()) {

            if (info.value.equals(value)) {

                return info;
            }
        }

        return PRIVACY;
    }
}
