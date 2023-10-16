package com.serendipity.seity.calling.dto.callingrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 소명 요청에 대해 페이징하여 조회할 떄 사용하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CallingRequestPagingResponse {

    private int totalPages;
    private int totalCallingNumber;
    private List<MultipleCallingRequestResponse> callings;
}
