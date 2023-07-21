package com.serendipity.seity.prompt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.member.service.MemberService;
import com.serendipity.seity.prompt.Qna;
import com.serendipity.seity.prompt.dto.QuestionRequest;
import com.serendipity.seity.prompt.service.ChatGptService;
import com.serendipity.seity.prompt.service.PromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Principal;

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

    @PostMapping(value = "/ask", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> ask(@RequestBody QuestionRequest question, Principal principal) {

        try {
            boolean isNewQuestion = question.init();    // 처음 하는 질문인지 여부

            Flux<String> answerFlux = chatGptService.ask(question.getSessionId(), question.getQuestion());
            String answerString = chatGptService.getAnswerStringFromFlux(answerFlux).toString();

            Mono<Void> saveAnswerMono =
                    Mono.fromRunnable(() -> {
                        try {
                            if (isNewQuestion) {        // 처음 하는 질문일 경우
                                promptService.saveAnswer(
                                        question.getSessionId(),
                                        new Qna(question.getQuestion(), answerString),
                                        memberService.getLoginMember(principal));
                            } else {                    // 이전 세션에 이어서 하는 질문일 경우
                                promptService.addAnswer(
                                        question.getSessionId(),
                                        new Qna(question.getQuestion(), answerString));
                            }
                        } catch (BaseException e) {
                            log.error("질문 저장 중 오류 발생: ", e);
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    });

            saveAnswerMono.subscribeOn(Schedulers.boundedElastic()).subscribe();

            return answerFlux;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return Flux.empty();
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
