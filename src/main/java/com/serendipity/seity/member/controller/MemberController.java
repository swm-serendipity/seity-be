package com.serendipity.seity.member.controller;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.member.dto.*;
import com.serendipity.seity.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

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
    public BaseResponse<?> signUp(@Valid @RequestBody SignUpRequest request) throws BaseException {

        return new BaseResponse<>(memberService.signUp(request));
    }

    @PostMapping("/login")
    public BaseResponse<?> login(@Valid @RequestBody LoginRequest request) throws BaseException {

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

    /**
     * 현재 로그인된 사용자의 정보를 반환하는 메서드입니다.
     * @param principal 인증 정보
     * @return 현재 로그인된 사용자의 정보
     */
    @GetMapping("/login/info")
    public BaseResponse<?> getLoginMemberInfo(Principal principal) throws BaseException {

        return new BaseResponse<>(MemberResponse.of(memberService.getLoginMember(principal)));
    }

    /**
     * 모든 직무를 반환하는 메서드입니다.
     * @return 모든 직무 리스트
     */
    @GetMapping("/member/part")
    public BaseResponse<?> getAllMemberParts() {

        return new BaseResponse<>(MemberPartResponse.getAllMemberParts());
    }

    /**
     * 멘션을 위해 모든 USER 권한을 가진 사용자를 반환하는 메서드입니다.
     * @return 사용자 리스트
     */
    @GetMapping("/member/role/user")
    public BaseResponse<?> getAllMemberForMention() {

        return new BaseResponse<>(memberService.getAllMemberForMention());
    }

    /**
     * 로그아웃 메서드입니다.
     * refresh token을 삭제합니다.
     * @param refreshToken refresh token
     * @return 성공 여부
     */
    @DeleteMapping("/user/logout")
    public BaseResponse<?> logout(@RequestParam String refreshToken) {

        memberService.deleteRefreshToken(refreshToken);
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 사용자 관리를 위해 사용자를 페이징하여 조회하는 메서드입니다.
     * @param pageNumber 페이지 번호 (0부터 시작)
     * @param pageSize 페이지 크기
     * @return 사용자 정보
     */
    @GetMapping("/admin/member/management")
    public BaseResponse<?> getPagingMember(@RequestParam int pageNumber, @RequestParam int pageSize) {

        return new BaseResponse<>(memberService.getPagingMember(pageNumber, pageSize));
    }

    /**
     * 사용자 정보를 수정하는 메서드입니다.
     * @param request 수정 정보
     * @return 결과
     * @throws BaseException 사용자 id가 올바르지 않을 경우
     */
    @PatchMapping("/admin/member")
    public BaseResponse<?> updateMember(@RequestBody UpdateMemberRequest request) throws BaseException {

        memberService.updateMember(request);
        return new BaseResponse<>(SUCCESS);
    }
}
