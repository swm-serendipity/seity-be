package com.serendipity.seity.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.post.Post;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Post 여러개를 조회할 떄 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultiplePostResponse {

    private String id;
    private String title;
    private String memberName;
    private String memberProfileBackgroundHex;
    private String memberProfileTextHex;
    private String memberPart;
    private int views;
    private String llm;
    private Qna firstQna;
    private boolean isLike;
    private int likeNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;

    public static MultiplePostResponse of(Post post, Prompt prompt, Member member, boolean isLike) {

        return new MultiplePostResponse(post.getId(), post.getTitle(),
                member.getName(), member.getProfileBackgroundHex(),
                member.getProfileTextHex(), member.getPart().getValue(), post.getViews(), prompt.getLlm(),
                prompt.getQnaList().get(0), isLike, post.getLikes().size(), post.getCreateTime(),
                post.getLastModifiedTime());
    }
}
