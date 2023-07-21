package com.serendipity.seity.prompt.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
import com.serendipity.seity.prompt.dto.ChatGptMessageRequest;
import com.serendipity.seity.prompt.dto.PromptResponse;
import com.serendipity.seity.prompt.repository.PromptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.seity.common.response.BaseResponseStatus.INVALID_USER_ACCESS_EXCEPTION;
import static com.serendipity.seity.prompt.Prompt.createPrompt;

/**
 * 프롬프트 관련 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PromptService {

    private final String USER_PREFIX = "user";
    private final String ASSISTANT_PREFIX = "assistant";

    private final PromptRepository promptRepository;

    /**
     * ChatGPT API로부터 받은 답변을 저장하는 메서드입니다.
     * TODO: LLM 추가 시 llm 필드 변수화
     * @param id 해당 document의 id
     * @param qna 질문과 답변 쌍
     * @param member 현재 로그인한 사용자
     */
    @Async
    public void saveAnswer(String id, Qna qna, Member member) {
        promptRepository.save(createPrompt(id, member.getId(), "ChatGPT", false, qna));
    }

    /**
     * sessionId를 기반으로 이전에 했던 모든 질문을 조회하는 메서드입니다,
     * @param sessionId session id
     * @return 이전에 했던 모든 질문들
     */
    public List<ChatGptMessageRequest> getPromptById(String sessionId) {

        Optional<Prompt> findPrompt = promptRepository.findById(sessionId);
        List<ChatGptMessageRequest> result = new ArrayList<>();

        if (findPrompt.isEmpty()) {
            return result;
        }

        for (Qna qna : findPrompt.get().getQnaList()) {

            result.add(new ChatGptMessageRequest(USER_PREFIX, qna.getQuestion()));
            result.add(new ChatGptMessageRequest(ASSISTANT_PREFIX, qna.getAnswer()));
        }

        return result;
    }

    /**
     * 이전 세션에 새로 한 질문을 추가하는 메서드입니다.
     * @param id document의 id
     * @param qna 새로 한 질문과 답변
     */
    @Async
    public void addAnswer(String id, Qna qna) {

        Optional<Prompt> findPrompt = promptRepository.findById(id);
        findPrompt.ifPresent(prompt -> {
            prompt.addQna(qna);
            promptRepository.save(prompt);
        });
    }

    /**
     * 최신 순으로 N개의 프롬프트 세션을 반환하는 메서드입니다.
     * @param userId 사용자 id
     * @param pageNumber page number
     * @param pageSize page size
     * @return 조회된 프롬프트 리스트
     */
    public List<PromptResponse> getLatestPromptsByUserId(String userId, int pageNumber, int pageSize) {

        Pageable pageable =
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "lastModifiedTime"));

        List<PromptResponse> result = new ArrayList<>();
        for (Prompt prompt: promptRepository.findByUserIdOrderByLastModifiedTimeDesc(userId, pageable)) {
            result.add(PromptResponse.of(prompt));
        }

        return result;
    }

    /**
     * 1개의 프롬프트 세션에 대해 즐겨찾기를 설정 또는 해제하는 메서드입니다.
     * @param id 프롬프트 세션 id
     * @param favorite 즐겨찾기 설정/해제 여부
     * @param member 로그인한 사용자
     * @throws BaseException 로그인한 사용자와 삭제하려는 프롬프트의 사용자 id가 다를 경우
     */
    public void setFavoritePrompt(String id, boolean favorite, Member member) throws BaseException {

        Prompt findPrompt = promptRepository.findById(id)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.INVALID_PROMPT_ID_EXCEPTION));

        if (!findPrompt.getUserId().equals(member.getId())) {
            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        findPrompt.setFavorite(favorite);
        promptRepository.save(findPrompt);
    }

    /**
     * 프롬프트 세션 1개를 삭제하는 메서드입니다.
     * @param id 프롬프트 세션 id
     * @param member 로그인한 사용자
     * @throws BaseException 로그인한 사용자와 삭제하려는 프롬프트의 사용자 id가 다를 경우
     */
    public void deletePrompt(String id, Member member) throws BaseException {

        Optional<Prompt> findPrompt = promptRepository.findById(id);

        if (findPrompt.isEmpty()) {
            return;
        }

        if (!findPrompt.get().getUserId().equals(member.getId())) {
            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        promptRepository.deleteById(id);
    }
}
