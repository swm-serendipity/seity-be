package com.serendipity.seity.forbiddenword.controller;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.forbiddenword.dto.CreateForbiddenWordRequest;
import com.serendipity.seity.forbiddenword.service.ForbiddenWordService;
import com.serendipity.seity.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;

/**
 * 금칙어 관련 controller 클래스입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/forbidden-word")
public class ForbiddenWordController {

    private final ForbiddenWordService forbiddenWordService;
    private final MemberService memberService;

    /**
     * 전체 금칙어 리스트를 조회하는 메서드입니다.
     * @return 전체 금칙어 리스트
     */
    @GetMapping
    public BaseResponse<?> getForbiddenWordList() {

        return new BaseResponse<>(forbiddenWordService.getForbiddenWords());
    }

    /**
     * 금칙어 1개를 추가하는 메서드입니다.
     * @param request 추가하려는 금칙어 정보
     * @param principal 인증 정보
     * @return 성공 여부
     * @throws BaseException 로그인한 사용자가 없을 경
     */
    @PostMapping
    public BaseResponse<?> createForbiddenWord(@RequestBody CreateForbiddenWordRequest request, Principal principal)
            throws BaseException {

        forbiddenWordService.createForbiddenWord(request, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 금칙어 1개를 삭제하는 메서드입니다.
     * @param id 금칙어 id
     * @return 성공 여부
     */
    @DeleteMapping
    public BaseResponse<?> deleteForbiddenWord(@RequestParam String id) {

        forbiddenWordService.deleteForbiddenWord(id);
        return new BaseResponse<>(SUCCESS);
    }
}
