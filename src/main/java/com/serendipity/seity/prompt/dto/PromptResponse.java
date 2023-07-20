package com.serendipity.seity.prompt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 프롬프트 조회 시 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PromptResponse {

    private String llm;
    private List<Qna> qnaList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;
    private boolean isFavorite;

    public static PromptResponse of(Prompt prompt) {
        return new PromptResponse(prompt.getLlm(), prompt.getQnaList(), prompt.getCreateTime(),
                prompt.getLastModifiedTime(), prompt.isFavorite());
    }
}
