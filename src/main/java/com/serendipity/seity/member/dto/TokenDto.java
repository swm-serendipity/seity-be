package com.serendipity.seity.member.dto;

import lombok.*;

/**
 * 생성된 토큰을 저장하는 dto 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String grantType;

    private String accessToken;

    private String refreshToken;
}
