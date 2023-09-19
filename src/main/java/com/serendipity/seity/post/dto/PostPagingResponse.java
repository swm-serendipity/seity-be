package com.serendipity.seity.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 여러개의 post를 페이징할 때 사용하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostPagingResponse {

    private int totalPages;
    private int totalPostNumber;
    private List<MultiplePostResponse> posts;
}
