package com.serendipity.seity.prompt.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponseStatus;
import com.serendipity.seity.config.ChatGptConfig;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.post.repository.PostRepository;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
import com.serendipity.seity.prompt.dto.ChatGptMessageRequest;
import com.serendipity.seity.prompt.dto.PromptPagingResponse;
import com.serendipity.seity.prompt.dto.PromptResponse;
import com.serendipity.seity.prompt.repository.PromptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.serendipity.seity.common.response.BaseResponseStatus.INVALID_PROMPT_ID_EXCEPTION;
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

    private final PromptRepository promptRepository;
    private final PostRepository postRepository;
    /*private final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
    private final Encoding enc = registry.getEncoding(EncodingType.CL100K_BASE);*/

    /**
     * 프롬프트 질의 1개를 저장하는 메서드입니다.
     * @param id 프롬프트 id
     * @param question 질문
     * @param answer 답변
     * @param member 현재 로그인한 사용자
     */
    public void savePrompt(String id, String question, String answer, Member member) throws BaseException {

        Optional<Prompt> findPrompt = promptRepository.findById(id);

        if (findPrompt.isEmpty()) {
            promptRepository.save(createPrompt(id, member.getId(), "ChatGPT",
                    new Qna(question, answer)));
            return;
        }

        findPrompt.get().addQna(new Qna(question, answer));
        promptRepository.save(findPrompt.get());
    }

    /**
     * 최신 순으로 N개의 프롬프트 세션을 반환하는 메서드입니다.
     * @param userId 사용자 id
     * @param pageNumber page number
     * @param pageSize page size
     * @return 조회된 프롬프트 리스트
     * TODO: 기존 for문 제거하고 람다식으로 진행
     * TODO: 포매팅을 제거 -> 원본 데이터를 넘겨버리고 클라이언트가 선택
     */
    public List<PromptPagingResponse> getLatestPromptsByUserId(String userId, int pageNumber, int pageSize) {

        Pageable pageable =
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "lastModifiedTime"));

        List<PromptPagingResponse> result = new ArrayList<>();
        for (Prompt prompt: promptRepository.findByUserIdAndIsExistOrderByLastModifiedTimeDesc(userId, true, pageable)) {
            result.add(PromptPagingResponse.of(prompt));
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

        if (postRepository.findByPromptId(id).isPresent()) {

            // 해당 프롬프트 세션으로 게시글이 생성된 경우 soft delete
            findPrompt.get().delete();
            promptRepository.save(findPrompt.get());
            return;
        }

        promptRepository.deleteById(id);
    }

    /**
     * 프롬프트 세션 1개를 조회하는 메서드입니다.
     * @param id 프롬프트 세션 id
     * @param member 현재 로그인한 사용자
     * @return 해당 프롬프트 세션의 정보
     * @throws BaseException 유효하지 않은 세션 id이거나, 프롬프트 작성자와 로그인한 사용자가 다른 경우
     */
    public PromptResponse getPromptById(String id, Member member) throws BaseException {

        Prompt findPrompt = promptRepository.findById(id)
                .orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        if (!findPrompt.isExist()) {
            throw new BaseException(INVALID_PROMPT_ID_EXCEPTION);
        }

        if (!findPrompt.getUserId().equals(member.getId())) {
            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        return PromptResponse.of(findPrompt);
    }

    /**
     * assistant request를 생성하는 메서드입니다.
     * @param id 프롬프트 세션 id
     * @return 생성된 assistant request
     * @throws BaseException 프롬프트 세션 id가 유효하지 않은 경우
     */
    public List<ChatGptMessageRequest> generateAssistantRequest(String id) throws BaseException {

        if (id == null) {
            return new ArrayList<>();
        }

        Prompt findPrompt =
                promptRepository.findById(id).orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        int currentTokenSize = 1000;
        List<ChatGptMessageRequest> result = new ArrayList<>();

        for (int i = findPrompt.getQnaList().size() - 1; i >= 0; i--) {

            if (findPrompt.getQnaList().get(i).getTokenNumber() + currentTokenSize > ChatGptConfig.MAX_TOKEN_SIZE) {
                break;
            }
            result.add(new ChatGptMessageRequest(ChatGptConfig.USER_ROLE,
                    findPrompt.getQnaList().get(i).getQuestion()));
            result.add(new ChatGptMessageRequest(ChatGptConfig.ASSISTANT_ROLE,
                    findPrompt.getQnaList().get(i).getAnswer()));
            currentTokenSize += findPrompt.getQnaList().get(i).getTokenNumber();
        }

        Collections.reverse(result);

        return result;
    }

    /**
     * continue generating을 할 때 assistant request를 생성하는 메서드입니다.
     * @param id 프롬프트 세션 id
     * @return 생성된 assistant request
     * @throws BaseException 프롬프트 세션 id가 유효하지 않은 경우
     */
    public List<ChatGptMessageRequest> generateAssistantRequestForContinueAsk(String id, Member member)
            throws BaseException {

        Prompt findPrompt =
                promptRepository.findById(id).orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        if (!findPrompt.getUserId().equals(member.getId())) {

            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        List<ChatGptMessageRequest> result = new ArrayList<>();

        result.add(new ChatGptMessageRequest(ChatGptConfig.USER_ROLE,
                findPrompt.getQnaList().get(findPrompt.getQnaList().size() - 1).getQuestion()));
        result.add(new ChatGptMessageRequest(ChatGptConfig.ASSISTANT_ROLE,
                findPrompt.getQnaList().get(findPrompt.getQnaList().size() - 1).getAnswer()));

        return result;
    }
}
