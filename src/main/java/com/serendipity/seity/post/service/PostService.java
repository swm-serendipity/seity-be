package com.serendipity.seity.post.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.post.Post;
import com.serendipity.seity.post.dto.PostResponse;
import com.serendipity.seity.post.repository.PostRepository;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.repository.PromptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.serendipity.seity.common.response.BaseResponseStatus.*;

/**
 * Post 관련 서비스 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PromptRepository promptRepository;
    private final PostRepository postRepository;

    /**
     * 새로운 게시글을 생성하는 메서드입니다.
     * @param promptId 공유하려는 프롬프트 id
     * @param member 현재 로그인한 사용자
     * @return 생성된 게시글의 id
     * @throws BaseException 유효하지 않은 prompt id이거나, 프롬프트의 작성자와 로그인한 사용자가 일치하지 않는 경우
     */
    public String createPost(String promptId, Member member) throws BaseException {

        Prompt findPrompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        if (!findPrompt.getUserId().equals(member.getId())) {
            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        return postRepository.save(Post.createPost(promptId, findPrompt.getQnaList().size() - 1)).getId();
    }

    /**
     * 게시글 1개를 조회하는 메서드입니다.
     * @param postId 게시글 id
     * @return 해당 게시글 정보
     * @throws BaseException 게시글 id가 유효햐지 않거나, 프롬프트 id가 유효하지 않은 경우
     */
    public PostResponse getPost(String postId) throws BaseException {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(INVALID_POST_ID_EXCEPTION));

        Prompt findPrompt = promptRepository.findById(findPost.getPromptId())
                .orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        return PostResponse.of(findPost, findPrompt);
    }

    /**
     * 게시글 1개를 삭제하는 메서드입니다.
     * @param postId 게시글 id
     */
    public void deletePost(String postId) {

        postRepository.deleteById(postId);
    }

}
