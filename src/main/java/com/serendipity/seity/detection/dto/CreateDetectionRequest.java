package com.serendipity.seity.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 민감정보 탐지 객체를 만들기 위해 사용되는 request 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDetectionRequest {

    private List<DetectionDto> detectionDtoList;
}
