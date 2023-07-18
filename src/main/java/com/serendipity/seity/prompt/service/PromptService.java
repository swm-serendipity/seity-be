package com.serendipity.seity.prompt.service;

import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Qna;
import com.serendipity.seity.prompt.repository.PromptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.serendipity.seity.prompt.Prompt.createPrompt;

/**
 * 프롬프트 관련 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;

    /**
     * ChatGPT API로부터 받은 답변을 저장하는 메서드입니다.
     * TODO: LLM 추가 시 llm 필드 변수화
     * @param qna 질문과 답변 쌍
     * @param member 현재 로그인한 사용자
     */
    @Async
    public void saveAnswer(Qna qna, Member member) {

        promptRepository.save(createPrompt(member.getId(), "ChatGPT", false, qna));
    }
}
