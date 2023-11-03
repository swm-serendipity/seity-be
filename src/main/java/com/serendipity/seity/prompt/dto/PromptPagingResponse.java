package com.serendipity.seity.prompt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 프롬프트를 페이징할 떄 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PromptPagingResponse {

    private int totalPages;
    private int totalPromptNumber;
    private int currentPageNumber;
    private List<SinglePromptPagingResponse> prompts;

}
