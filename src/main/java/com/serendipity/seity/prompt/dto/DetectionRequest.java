package com.serendipity.seity.prompt.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프롬프트 질문 시 민감정보 탐지 내역을 넘겨주기 위한 request 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetectionRequest {

    private int index;
    private int length;
    @NotNull
    private Boolean isDeIdentified;
    private String entity;
}
