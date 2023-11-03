package com.serendipity.seity.calling.dto.calling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 메시지 관리의 소명들에 대해 paging하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CallingPagingResponse {

    private int totalPages;
    private int totalCallingNumber;
    private int currentPageNumber;
    private List<MultipleCallingResponse> callings;
}
