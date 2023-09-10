package com.serendipity.seity.detection.controller;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.common.response.BaseResponseStatus;
import com.serendipity.seity.detection.service.PromptDetectionService;
import com.serendipity.seity.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;

/**
 * 중요정보 탐지 관련 컨트롤러 클래스입니다.
 * TODO: 소명 요청 API
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/detection")
public class PromptDetectionController {

    private final PromptDetectionService promptDetectionService;
    private final MemberService memberService;

    /**
     * 민감정보 탐지 내역 리스트를 조회하는 메서드입니다.
     * @param principal 인증 정보
     * @return 민감정보 탐지 내역 리스트
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @GetMapping
    public BaseResponse<?> getDetectionList(Principal principal) throws BaseException {

        return new BaseResponse<>(promptDetectionService.getDetectionList(memberService.getLoginMember(principal)));
    }

    /**
     * 1개의 민감정보 탐지 내역을 조회하는 메서드입니다.
     * @param id 민감정보 탐지 내역 id
     * @param principal 인증 정보
     * @return 민감정보 탐지 내역
     */
    @GetMapping("/{id}")
    public BaseResponse<?> getSingleDetection(@PathVariable String id, Principal principal) throws BaseException {

        return new BaseResponse<>(promptDetectionService.getSingleDetection(
                id, memberService.getLoginMember(principal)));
    }

    /**
     * 1개의 민감정보 탐지 내역에 대해 무시하는 메서드입니다.
     * @param id 민감정보 탐지 내역 id
     * @param principal 인증 정보
     * @return 삭제 결과
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @DeleteMapping
    public BaseResponse<?> ignoreDetection(@RequestParam String id, Principal principal) throws BaseException {

        promptDetectionService.deletePromptDetection(id, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }
}
