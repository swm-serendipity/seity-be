package com.serendipity.seity.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 검출된 1개의 민감정보를 표현하는 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetectionDto {

    private int index;
    private int length;
    private boolean isDeIdentified;
    private String detectionInfo;
}
