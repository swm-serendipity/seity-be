package com.serendipity.seity.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 게시글 생성 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {

    private String id;
    private String title;
    private List<MentionMemberRequest> mentionMemberList;
}
