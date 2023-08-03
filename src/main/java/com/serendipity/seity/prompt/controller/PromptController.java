package com.serendipity.seity.prompt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.config.ChatGptConfig;
import com.serendipity.seity.member.service.MemberService;
import com.serendipity.seity.prompt.dto.*;
import com.serendipity.seity.prompt.service.ChatGptService;
import com.serendipity.seity.prompt.service.PromptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;

/**
 * 프롬프트 컨트롤러입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/prompt")
@Slf4j
public class PromptController {

    private final PromptService promptService;
    private final MemberService memberService;
    private final ChatGptService chatGptService;

    /**
     * 프롬프트 질의 메서드입니다.
     * @param request 세션 id, 질문
     * @param principal 인증 정보
     * @return 답변 stream
     * @throws BaseException 로그인한 사용자가 유효하지 않거나, session id 가 유효하지 않은 경우
     */
    @PostMapping(value = "/ask", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> ask(@Valid @RequestBody PromptAskRequest request, Principal principal)
            throws BaseException {

        try {
            List<ChatGptMessageRequest> previousPromptList = request.getQuestion() == null
                    ? new ArrayList<>() : promptService.generateAssistantRequest(request.getSessionId());

            request.init();

            Flux<String> responseFlux = chatGptService.ask(
                    previousPromptList,
                    request.getSessionId(),
                    request.getQuestion(),
                    memberService.getLoginMember(principal)
            );

            return ResponseEntity.ok()
                    .header(ChatGptConfig.NGINX_NO_BUFFERING_HEADER,
                            ChatGptConfig.NGINX_NO_BUFFERING_HEADER_VALUE) // nginx 버퍼링 해제
                    .body(responseFlux);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok()
                    .body(Flux.empty());
        }
    }

    /**
     * 답변이 잘린 경우, 이어서 답변하는 메서드입니다.
     * @param sessionId 세션 id
     * @param principal 인증 정보
     * @return 답변 stream
     * @throws BaseException 로그인한 사용자가 유효하지 않거나, session id 가 유효하지 않은 경우
     */
    @PostMapping(value = "/ask/continue", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> continueAsk(@RequestParam String sessionId, Principal principal)
            throws BaseException {

        try {
            List<ChatGptMessageRequest> previousPromptList = promptService.generateAssistantRequestForContinueAsk(
                    sessionId,
                    memberService.getLoginMember(principal));

            Flux<String> responseFlux = chatGptService.ask(
                    previousPromptList,
                    sessionId,
                    null,
                    memberService.getLoginMember(principal)
            );

            return ResponseEntity.ok()
                    .header(ChatGptConfig.NGINX_NO_BUFFERING_HEADER,
                            ChatGptConfig.NGINX_NO_BUFFERING_HEADER_VALUE) // nginx 버퍼링 해제
                    .body(responseFlux);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok()
                    .body(Flux.empty());
        }
    }

    /**
     * 최근 작성한 프롬프트 리스트를 조회하는 메서드입니다.
     * @param pageNumber page number
     * @param pageSize page size
     * @param principal 인증 정보
     * @return 프롬프트 리스트
     */
    @GetMapping("/history")
    public BaseResponse<?> getRecentPromptList(@RequestParam int pageNumber, @RequestParam int pageSize,
                                               Principal principal) throws BaseException {

        return new BaseResponse<>(promptService.getLatestPromptsByUserId(
                memberService.getLoginMember(principal).getId(), pageNumber, pageSize));
    }

    /**
     * 프롬프트 세션 1개에 대해 즐겨찾기를 하는 메서드입니다.
     * @param sessionId 프롬프트 세션 id
     * @param principal 인증 정보
     * @return 성공 결과
     * @throws BaseException id가 유효하지 않거나, 권한이 없는 유저가 접근한 경우
     */
    @PostMapping("/favorite")
    public BaseResponse<?> setFavoritePrompt(@RequestParam String sessionId, Principal principal) throws BaseException {

        promptService.setFavoritePrompt(sessionId, true, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 프롬프트 세션 1개에 대해 즐겨찾기를 해제하는 메서드입니다.
     * @param sessionId 프롬프트 세션 id
     * @param principal 인증 정보
     * @return 성공 결과
     * @throws BaseException id가 유효하지 않거나, 권한이 없는 유저가 접근한 경우
     */
    @DeleteMapping("/favorite")
    public BaseResponse<?> unsetFavoritePrompt(@RequestParam String sessionId, Principal principal) throws BaseException {

        promptService.setFavoritePrompt(sessionId, false, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 프롬프트 세션 1개를 삭제하는 메서드입니다.
     * @param sessionId 프롬프트 세션 id
     * @param principal 인증 정보
     * @return 성공 결과
     * @throws BaseException 권한이 없는 유저가 접근한 경우
     */
    @DeleteMapping
    public BaseResponse<?> deletePrompt(@RequestParam String sessionId, Principal principal) throws BaseException {

        promptService.deletePrompt(sessionId, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 프롬프트 세션 1개를 조회하는 메서드입니다.
     * @param sessionId 프롬프트 세션 id
     * @param principal 인증 정보
     * @return 프롬프트 정보
     * @throws BaseException 유효하지 않은 세션 id이거나, 로그인한 사용자와 프롬프트 작성자가 다른 경우
     */
    @GetMapping
    public BaseResponse<?> getSinglePrompt(@RequestParam String sessionId, Principal principal) throws BaseException {

        return new BaseResponse<>(promptService.getPromptById(sessionId, memberService.getLoginMember(principal)));
    }
}
