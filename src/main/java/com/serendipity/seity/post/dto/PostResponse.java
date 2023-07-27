package com.serendipity.seity.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.post.Post;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Post 조회에 사용되는 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private String id;
    private int views;
    private String llm;
    private List<Qna> qnaList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;

    public static PostResponse of(Post post, Prompt prompt) {

        List<Qna> qnas = new ArrayList<>();
        for (int i = 0; i <= post.getIndex(); i++) {
            qnas.add(prompt.getQnaList().get(i));
        }
        return new PostResponse(post.getId(), post.getViews(), prompt.getLlm(), qnas, post.getCreateTime(),
                post.getLastModifiedTime());
    }
}
