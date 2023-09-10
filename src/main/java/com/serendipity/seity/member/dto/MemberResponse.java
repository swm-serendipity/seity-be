package com.serendipity.seity.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.serendipity.seity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * member 엔티티에 대한 조회를 할때 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {

    private String id;
    private String name;
    private String loginId;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDate birthDate;
    private String part;
    private String profileBackgroundHex;
    private String profileTextHex;

    public static MemberResponse of(Member member) {

        return new MemberResponse(member.getId(), member.getName(), member.getLoginId(), member.getEmail(),
                member.getBirthDate(), member.getPart().getValue(), member.getProfileBackgroundHex(),
                member.getProfileTextHex());
    }
}
