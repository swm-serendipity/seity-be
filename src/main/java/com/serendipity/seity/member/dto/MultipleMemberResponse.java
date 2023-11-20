package com.serendipity.seity.member.dto;

import com.serendipity.seity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 사용자 관리 페이징을 위한 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MultipleMemberResponse {

    private String id;
    private String loginId;
    private String name;
    private String part;
    private List<String> role;

    public static MultipleMemberResponse of(Member member) {

        return new MultipleMemberResponse(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getPart().getName(),
                member.getRoles());
    }
}
