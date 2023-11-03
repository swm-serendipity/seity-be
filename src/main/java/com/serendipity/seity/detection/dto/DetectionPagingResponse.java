package com.serendipity.seity.detection.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 민감정보 탐지 내역을 페이징하여 조회할 때 사용하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetectionPagingResponse {

    private int totalPages;
    private int totalDetectionNumber;
    private int currentPageNumber;
    private List<MultipleDetectionResponse> detections;
}
