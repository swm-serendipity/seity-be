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
import java.util.ArrayList;
import java.util.List;

/**
 * 1개의 Post 조회에 사용되는 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private String id;
    private String title;
    private String memberName;
    private String memberProfileBackgroundHex;
    private String memberProfileTextHex;
    private String memberPart;
    private int views;
    private String llm;
    private List<Qna> qnaList;
    private boolean isLike;
    private boolean isScrap;
    private boolean isMyPost;
    private int likeNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;

    public static PostResponse of(Post post, Prompt prompt, Member member, boolean isLike, boolean isScrap, boolean isMyPost) {

        List<Qna> qnas = new ArrayList<>();
        for (int i = 0; i <= post.getIndex(); i++) {
            qnas.add(prompt.getQnaList().get(i));
        }
        return new PostResponse(post.getId(), post.getTitle(), member.getName(), member.getProfileBackgroundHex(),
                member.getProfileTextHex(), member.getPart().getValue(), post.getViews(), prompt.getLlm(),
                qnas, isLike, isScrap, isMyPost, post.getLikeNumber(), post.getCreateTime(), post.getLastModifiedTime());
    }
}
