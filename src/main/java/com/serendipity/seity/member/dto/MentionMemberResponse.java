package com.serendipity.seity.member.dto;

import com.serendipity.seity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자를 멘션할때 사용되는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MentionMemberResponse {

    private String id;
    private String name;
    private String part;
    private String profileBackgroundHex;
    private String profileTextHex;

    public static MentionMemberResponse of(Member member) {

        return new MentionMemberResponse(member.getId(), member.getName(), member.getPart().getValue(),
                member.getProfileBackgroundHex(), member.getProfileTextHex());
    }
}
