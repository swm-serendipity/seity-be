package com.serendipity.seity.dpr.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DPR 테스트 데이터 관련 탐지 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DprDetectionResponse {

    String docs;
    String content;
    String link;
    int similarity;
}
