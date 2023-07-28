package com.serendipity.seity.dlp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DLP를 위한 resposne 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@Setter
@NoArgsConstructor
public class DlpResponse {

    private String originalQuestion;
    private String convertedQuestion;
    private List<DeIdentificationResult> result = new ArrayList<>();

    public DlpResponse(String originalQuestion, String convertedQuestion) {
        this.originalQuestion = originalQuestion;
        this.convertedQuestion = convertedQuestion;
    }
}
