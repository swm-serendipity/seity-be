package com.serendipity.seity.prompt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
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
public class PromptPagingResponse {

    private String id;
    private String llm;
    private String firstQuestion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;
    private boolean isFavorite;

    public static PromptPagingResponse of(Prompt prompt) {
        return new PromptPagingResponse(prompt.getId(), prompt.getLlm(), prompt.getQnaList().get(0).getQuestion(),
                prompt.getCreateTime(), prompt.getLastModifiedTime(), prompt.isFavorite());
    }
}
