package com.serendipity.seity.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 사용자 페이징 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberPagingResponse {

    private int totalPages;
    private int totalMemberNumber;
    private int currentPageNumber;
    private List<MultipleMemberResponse> members;
}
