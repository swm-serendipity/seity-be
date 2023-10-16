package com.serendipity.seity.calling.controller;

import com.serendipity.seity.calling.dto.callingrequest.CallingRequestRequest;
import com.serendipity.seity.calling.dto.calling.CallingSendRequest;
import com.serendipity.seity.calling.service.CallingService;
import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.time.LocalDateTime;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;
import static com.serendipity.seity.config.ChatGptConfig.NGINX_NO_BUFFERING_HEADER;

/**
 * 소명 관련 컨트롤러 클래스입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/calling")
public class CallingController {

    private final CallingService callingService;
    private final MemberService memberService;

    /**
     * SSE 방식을 사용하기 위해 클라이언트가 구독 요청을 하는 메서드입니다.
     * @param principal 인증 정보
     * @return 생성된 sse emitter
     * @throws BaseException 현재 로그인한 사용자가 없을 경우
     */
    @GetMapping("/subscribe")
    public SseEmitter alarmSubscribe(Principal principal, HttpServletResponse response) throws BaseException {

        response.setHeader(NGINX_NO_BUFFERING_HEADER, "no");
        return callingService.subscribe(memberService.getLoginMember(principal), LocalDateTime.now());
    }

    /**
     * 소명 요청을 보내는 메서드입니다.
     * @param request 소명 요청 id
     * @param principal 인증 정보
     * @return 성공 여부
     * @throws BaseException 현재 로그인한 사용자가 없을 경우
     */
    @PostMapping
    public BaseResponse<?> sendCallingRequest(@RequestBody CallingRequestRequest request, Principal principal) throws BaseException {

        callingService.createCalling(request.getPromptDetectionId(), memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 특정 사용자의 소명 요청 리스트를 조회하는 메서드입니다.
     * @param pageNumber 페이지 번호 (0부터 시작)
     * @param pageSize 페이지 크기
     * @param principal 인증 정보
     * @return 소명 요청 리스트
     * @throws BaseException 현재 로그인한 사용자가 없을 경우
     */
    @GetMapping("/all")
    public BaseResponse<?> getAllCallings(@RequestParam int pageNumber, @RequestParam int pageSize, Principal principal)
            throws BaseException {

        return new BaseResponse<>(callingService.getCallingList(pageNumber, pageSize, memberService.getLoginMember(principal)));
    }

    /**
     * 특정 사용자의 단일 소명을 조회하는 메서드입니다.
     * @param id 소명 id
     * @param principal 인증 정보
     * @return 소명 정보
     * @throws BaseException 현재 로그인한 사용자가 없을 경우
     */
    @GetMapping
    public BaseResponse<?> getSingleCalling(@RequestParam String id, Principal principal) throws BaseException {

        return new BaseResponse<>(callingService.getSingleCalling(id, memberService.getLoginMember(principal)));
    }

    /**
     * 1개의 소명 요청에 대해 소명을 전송하는 메서드입니다.
     * @param request 소명 정보
     * @param principal 인증 정보
     * @return 성공 여부
     * @throws BaseException 현재 로그인한 사용자가 없을 경우
     */
    @PostMapping("/send")
    public BaseResponse<?> sendCalling(@RequestBody CallingSendRequest request, Principal principal) throws BaseException {

        callingService.sendCallingContent(request.getId(), memberService.getLoginMember(principal), request.getContent());
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 보안 담당자가 소명 & 소명 요청 히스토리 리스트를 페이징하여 조회하는 메서드입니다.
     * @param pageNumber 페이지 번호 (0부터 시작)
     * @param pageSize 페이지 크기
     * @param principal 인증 정보
     * @return 소명 히스토리
     */
    @GetMapping("/admin/history")
    public BaseResponse<?> getCallingHistory(@RequestParam int pageNumber, int pageSize, Principal principal) throws BaseException {

        return new BaseResponse<>(callingService.getCallingHistory(
                pageNumber,
                pageSize,
                memberService.getLoginMember(principal)
        ));
    }

    /**
     * 단일 소명 히스토리를 조회하는 메서드입니다.
     * @param id 소명 id
     * @return 소명 히스토리
     */
    @GetMapping("/admin/history/{id}")
    public BaseResponse<?> getSingleCallingHistory(@PathVariable String id) throws BaseException {

        return new BaseResponse<>(callingService.getSingleCallingHistory(id));
    }

    /**
     * 단일 소명에 대해 허가하는 메서드입니다.
     * @param id 소명 id
     * @return 성공 여부
     */
    @PatchMapping("/solve")
    public BaseResponse<?> solveSingleCalling(@RequestParam String id) throws BaseException {

        callingService.solveCalling(id);
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 단일 소명에 대해 삭제하는 메서드입니다.
     * @param id 소명 id
     * @return 성공 여부
     */
    @DeleteMapping
    public BaseResponse<?> deleteSingleCalling(@RequestParam String id) {

        callingService.deleteCalling(id);
        return new BaseResponse<>(SUCCESS);
    }
}
