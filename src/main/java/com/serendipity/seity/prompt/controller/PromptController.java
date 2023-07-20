package com.serendipity.seity.prompt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serendipity.seity.common.exception.BaseException;
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
            Flux<String> answerFlux = chatGptService.ask(question.getQuestion());
            String answerString = chatGptService.getAnswerStringFromFlux(answerFlux).toString();

            Mono<Void> saveAnswerMono =
                    Mono.fromRunnable(() -> {
                        try {
                            promptService.saveAnswer(new Qna(question.getQuestion(), answerString),
                                    memberService.getLoginMember(principal));
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
}
