package com.serendipity.seity.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보 수정을 위한 request 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberRequest {

    private String id;
    private String name;
    private String part;
    private String role;
}
