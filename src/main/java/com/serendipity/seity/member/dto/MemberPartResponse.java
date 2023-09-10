package com.serendipity.seity.member.dto;

import com.serendipity.seity.member.MemberPart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 직무에 대해 반환하는 response 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberPartResponse {

    private String name;
    private String value;

    public static List<MemberPartResponse> getAllMemberParts() {

        List<MemberPartResponse> result = new ArrayList<>();
        for (MemberPart part : MemberPart.values()) {

            result.add(new MemberPartResponse(part.getName(), part.getValue()));
        }

        return result;
    }
}
