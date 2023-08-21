package com.serendipity.seity.post.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.member.repository.MemberRepository;
import com.serendipity.seity.post.Like;
import com.serendipity.seity.post.Post;
import com.serendipity.seity.post.dto.*;
import com.serendipity.seity.post.repository.PostRepository;
import com.serendipity.seity.prompt.Prompt;
import com.serendipity.seity.prompt.Qna;
import com.serendipity.seity.prompt.repository.PromptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final MemberRepository memberRepository;

    /**
     * 새로운 게시글을 생성하는 메서드입니다.
     * @param promptId 공유하려는 프롬프트 id
     * @param title 게시글 제목
     * @param mentionMemberList 멘션하려는 사용자 리스트
     * @param member 현재 로그인한 사용자
     * @return 생성된 게시글의 id
     * @throws BaseException 유효하지 않은 prompt id이거나, 프롬프트의 작성자와 로그인한 사용자가 일치하지 않는 경우
     */
    public CreatePostResponse createPost(String promptId, String title, List<MentionMemberRequest> mentionMemberList,
                                         Member member) throws BaseException {

        Prompt findPrompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        if (!findPrompt.getUserId().equals(member.getId())) {
            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        return new CreatePostResponse(postRepository.save(
                        Post.createPost(
                                promptId,
                                findPrompt.getQnaList().size() - 1,
                                member.getPart().getValue(),
                                title,
                                mentionMemberList.stream()
                                        .map(MentionMemberRequest::getMemberId)
                                        .collect(Collectors.toList())))
                .getId());
    }

    /**
     * 가장 최근의 n개의 게시글을 조회하는 메서드입니다.
     * @param pageNumber page 인덱스
     * @param pageSize page 크기
     * @param member 현재 로그인한 사용자
     * @return 조회 결과
     * @throws BaseException 프롬프트 id가 유효하지 않을 경우
     */
    public List<MultiplePostResponse> getRecentPosts(int pageNumber, int pageSize, Member member)
            throws BaseException {

        Pageable pageable =
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));

        return pagingPosts(postRepository.findByOrderByCreateTimeDesc(pageable), member);
    }

    /**
     * 7일 이내의 프롬프트 중 좋아요가 많은 순서대로 n개를 조회하는 메서드입니다.
     * @param n 몇개를 조회할지
     * @param member 현재 로그인한 사용자
     * @return 조회 결과
     * @throws BaseException 프롬프트 id가 유효하지 않을 경우
     */
    public List<MultiplePostResponse> getPopularRecentPosts(int n, Member member) throws BaseException {

        Pageable pageable = PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "likeNumber"));
        return pagingPosts(postRepository.findTopNByOrderByLikeNumberDesc(LocalDateTime.now().minusDays(7),
                pageable), member);

    }

    /**
     * 우리 부서내 7일 이내의 프롬프트 중 좋아요가 많은 순서대로 n개를 조회하는 메서드입니다.
     * @param n 몇개를 조회할지
     * @param member 현재 로그인한 사용자
     * @return 조회 결과
     */
    public List<MultiplePostResponse> getMyPartPopularRecentPosts(int n, Member member) throws BaseException {

        Pageable pageable = PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "likeNumber"));
        return pagingPosts(postRepository.findByPartOrderByLikeNumberDesc(member.getPart().getValue(),
                LocalDateTime.now().minusDays(7), pageable), member);
    }

    /**
     * 전체 기간 중 좋아요가 많은 순서대로 n개를 조회하는 메서드입니다.
     * @param n 몇개를 조회할지
     * @param member 현재 로그인한 사용자
     * @return 조회 결과
     * @throws BaseException 프롬프트 id가 유효하지 않을 경우
     */
    public List<MultiplePostResponse> getPopularPosts(int n, Member member) throws BaseException {

        Pageable pageable = PageRequest.of(0, n, Sort.by(Sort.Direction.DESC, "likeNumber"));
        return pagingPosts(postRepository.findByOrderByLikesDesc(pageable), member);
    }

    /**
     * 게시글 1개를 조회하는 메서드입니다.
     * @param postId 게시글 id
     * @return 해당 게시글 정보
     * @throws BaseException 게시글 id가 유효햐지 않거나, 프롬프트 id가 유효하지 않은 경우
     */
    public PostResponse getSinglePost(String postId, Member member) throws BaseException {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(INVALID_POST_ID_EXCEPTION));

        Prompt findPrompt = promptRepository.findById(findPost.getPromptId())
                .orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        if (!findPrompt.getUserId().equals(member.getId())) {   // 조회수 증가
            findPost.increaseViews();
            postRepository.save(findPost);
        }

        return PostResponse.of(findPost, findPrompt, memberRepository.findById(findPrompt.getUserId()).orElseThrow(
                () -> new BaseException(INVALID_MEMBER_ID_EXCEPTION)), isLike(findPost, member));
    }

    /**
     * 좋아요 메서드입니다.
     * @param postId 게시글 id
     * @param member 현재 로그인한 사용자
     * @throws BaseException 게시글 id가 유효하지 않은 경우
     */
    public void like(String postId, Member member) throws BaseException {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(INVALID_POST_ID_EXCEPTION));

        if (isLike(findPost, member)) {

            return;
        }

        findPost.like(member);
        postRepository.save(findPost);
    }

    /**
     * 좋아요 취소 메서드입니다.
     * @param postId 게시글 id
     * @param member 현재 로그인한 사용자
     * @throws BaseException 게시글 id가 유효하지 않은 경우
     */
    public void unlike(String postId, Member member) throws BaseException {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(INVALID_POST_ID_EXCEPTION));

        if (!isLike(findPost, member)) {

            return;
        }

        findPost.unlike(member);
        postRepository.save(findPost);
    }

    /**
     * 게시글 1개를 삭제하는 메서드입니다.
     * @param postId 게시글 id
     * @throws BaseException 게시글을 삭제하려는 주체가 게시글을 작성한 사용자가 아닌 경우
     */
    public void deletePost(String postId, Member member) throws BaseException {

        if (!promptRepository.findById(
                postRepository.findById(postId).orElseThrow(() -> new BaseException(INVALID_POST_ID_EXCEPTION))
                        .getPromptId()).orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION))
                .getUserId().equals(member.getId())) {

            throw new BaseException(INVALID_USER_ACCESS_EXCEPTION);
        }

        postRepository.deleteById(postId);
    }

    /**
     * 1개의 게시글에 대해 자신의 프롬프트 히스토리에 추가하는 메서드입니다.
     * @param postId 게시글 id
     * @param member 현재 로그인한 사용자
     * @return 추가된 세션의 id
     */
    public ImportPostResponse importPost(String postId, Member member) throws BaseException {


        Post findPost = postRepository.findById(postId).orElseThrow(
                () -> new BaseException(INVALID_POST_ID_EXCEPTION));

        Prompt findPrompt = promptRepository.findById(findPost.getPromptId()).orElseThrow(
                () -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

        List<Qna> qnas = new ArrayList<>();

        for (int i = 0; i <= findPost.getIndex(); i++) {

            qnas.add(new Qna(findPrompt.getQnaList().get(i).getQuestion(),
                    findPrompt.getQnaList().get(i).getAnswer()));
        }

        return new ImportPostResponse(promptRepository.save(Prompt.importPrompt(member.getId(),
                findPrompt.getLlm(), qnas)).getId());
    }

    /**
     * 해당 게시글에 대해 좋아요를 눌렀는지 여부를 반환하는 메서드입니다.
     * @param post 게시글
     * @param member 사용자
     * @return 좋아요 여부
     */
    private boolean isLike(Post post, Member member) {

        for (Like like : post.getLikes()) {

            if (like.getUserId().equals(member.getId())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Post 리스트로부터 반환할 response 클래스로 변환하는 메서드입니다.
     * @param posts 포스트 리스트
     * @param member 현재 로그인한 사용자
     * @return 변환된 response
     * @throws BaseException 프롬프트 id가 유효하지 않은 경우
     */
    private List<MultiplePostResponse> pagingPosts(List<Post> posts, Member member) throws BaseException {

        List<MultiplePostResponse> result = new ArrayList<>();
        for (Post post : posts) {
            Prompt findPrompt = promptRepository.findById(post.getPromptId())
                    .orElseThrow(() -> new BaseException(INVALID_PROMPT_ID_EXCEPTION));

            Optional<Member> findMember = memberRepository.findById(findPrompt.getUserId());
            if (findMember.isEmpty()) {
                continue;
            }

            result.add(MultiplePostResponse.of(
                    post,
                    findPrompt,
                    findMember.get(),
                    isLike(post, member)))
            ;
        }

        return result;
    }
}
