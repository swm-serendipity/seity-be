package com.serendipity.seity.dlp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 비식별화 결과를 담기 위한 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeIdentificationResult {

    private int index;
    private int length;
}
