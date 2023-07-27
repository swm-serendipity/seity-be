package com.serendipity.seity.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 회원가입 시 사용되는 request 객체입니다.
 *
 * @author Min Ho CHO
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$",
            message = "로그인 id는 시작은 영문으로만 가능하고, '_'를 제외한 특수문자는 사용할 수 없으며" +
                    " 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하여야 합니다.")
    private String loginId;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$",
            message = "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birthDate;

    private String memberRole;
    private String part;
}
