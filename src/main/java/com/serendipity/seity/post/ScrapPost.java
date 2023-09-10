package com.serendipity.seity.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 스크랩한 게시글을 나타내는 엔티티 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScrapPost {

    private String postId;
    private LocalDateTime createdAt;
}
