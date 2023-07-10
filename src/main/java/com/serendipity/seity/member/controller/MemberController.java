package com.serendipity.seity.member.controller;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.member.dto.LoginRequest;
import com.serendipity.seity.member.dto.SignUpRequest;
import com.serendipity.seity.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * member 관련 컨트롤러입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 메서드입니다.
     * @param request 회원가입 request 객체
     * @return 회원가입 결과
     */
    @PostMapping("/signup")
    public BaseResponse<?> signUp(@RequestBody SignUpRequest request) throws BaseException {

        return new BaseResponse<>(memberService.signUp(request));
    }

    @PostMapping("/login")
    public BaseResponse<?> login(@RequestBody LoginRequest request) throws BaseException {

        return new BaseResponse<>(memberService.login(request));
    }
}
