package com.serendipity.seity.member.controller;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.common.response.BaseResponseStatus;
import com.serendipity.seity.member.dto.LoginRequest;
import com.serendipity.seity.member.dto.SignUpRequest;
import com.serendipity.seity.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;


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
    public BaseResponse<?> login(@RequestBody LoginRequest request) {

        return new BaseResponse<>(memberService.login(request));
    }

    /**
     * access token 재발급 메서드입니다.
     */
    @GetMapping("/auth/reissue")
    public BaseResponse<?> reissueToken(HttpServletRequest request) throws BaseException {

        return new BaseResponse<>(memberService.reissueToken(request));
    }

    /**
     * 로그인 테스트용 메서드입니다.
     */
    @GetMapping("/login/test")
    public BaseResponse<?> loginTest() {

        return new BaseResponse<>(SUCCESS);
    }
}
