package com.serendipity.seity.prompt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.prompt.Prompt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 프롬프트 히스토리를 조회할 때 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SinglePromptPagingResponse {

    private String id;
    private String llm;
    private String firstQuestion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;
    private boolean isFavorite;

    public static SinglePromptPagingResponse of(Prompt prompt) {
        return new SinglePromptPagingResponse(prompt.getId(), prompt.getLlm(), prompt.getQnaList().get(0).getQuestion(),
                prompt.getCreateTime(), prompt.getLastModifiedTime(), prompt.isFavorite());
    }
}
