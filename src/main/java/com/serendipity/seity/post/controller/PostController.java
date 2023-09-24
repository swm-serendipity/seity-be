package com.serendipity.seity.post.controller;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.common.response.BaseResponse;
import com.serendipity.seity.member.service.MemberService;
import com.serendipity.seity.post.dto.CreatePostRequest;
import com.serendipity.seity.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.serendipity.seity.common.response.BaseResponseStatus.SUCCESS;

/**
 * 게시글 관련 컨트롤러 클래스입니다.
 *
 * @author Min Ho CHO
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    /**
     * 게시글 생성 메서드입니다.
     * TODO: 멘션한 사용자에게 알림
     * @param request 공유할 프롬프트 정보
     * @param principal 인증 정보
     * @return 생성된 게시글 id
     * @throws BaseException 로그인한 사용자가 없거나, 타인의 프롬프트를 공유하려고 시도한 경우
     */
    @PostMapping
    public BaseResponse<?> createPost(@RequestBody CreatePostRequest request, Principal principal) throws BaseException {

        return new BaseResponse<>(postService.createPost(request.getId(), request.getTitle(),
                request.getMentionMemberList(), memberService.getLoginMember(principal)));
    }

    /**
     * 1개의 게시글을 조회하는 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 게시글 정보
     * @throws BaseException 로그인한 사용자가 없는 경우
     */
    @GetMapping
    public BaseResponse<?> getPost(@RequestParam String postId, Principal principal) throws BaseException {

        return new BaseResponse<>(postService.getSinglePost(postId, memberService.getLoginMember(principal)));
    }

    /**
     * 게시글을 페이징하여 가져오는 메서드입니다.
     * @param pageNumber page 인덱스
     * @param pageSize page 크기
     * @param principal 인증 정보
     * @throws BaseException 로그인한 사용자가 없는 경우
     * @return 게시글 리스트
     */
    @GetMapping("/feed")
    public BaseResponse<?> getRecentPosts(@RequestParam int pageNumber, @RequestParam int pageSize,
                                          Principal principal) throws BaseException {

        return new BaseResponse<>(postService.getRecentPosts(pageNumber, pageSize,
                memberService.getLoginMember(principal)));
    }

    /**
     * size개 만큼의 인기 게시글을 가져오는 메서드입니다.
     * 인기 게시글이란, 7일 이내에 작성된 게시글 중 추천수가 많은 순서대로 정렬된 게시글을 의미합니다.
     * @param size 가져올 게시글의 개수
     * @param principal 인증 정보
     * @return 게시글 리스트
     * @throws BaseException 로그인한 사용자가 없는 경우
     */
    @GetMapping("/feed/top")
    public BaseResponse<?> getRecentPopularPosts(@RequestParam int size, Principal principal)
            throws BaseException {

        return new BaseResponse<>(postService.getPopularRecentPosts(size, memberService.getLoginMember(principal)));
    }

    /**
     * size 만큼의 인기 게시글을 가져오는 메서드입니다.
     * 여기서의 인기 게시글은 전체 기간에서의 인기 게시글을 의미합니다.
     * @param size 가져올 게시글의 개수
     * @param principal 인증 정보
     * @return 게시글 리스트
     * @throws BaseException 로그인한 사용자가 없는 경우
     */
    @GetMapping("/feed/top/all")
    public BaseResponse<?> getPopularPosts(@RequestParam int size, Principal principal) throws BaseException {

        return new BaseResponse<>(postService.getPopularPosts(size, memberService.getLoginMember(principal)));
    }

    /**
     * 로그인한 사용자의 부서 내에서 작성한 게시글 중 size만큼의 최신 인기글을 가져오는 메서드입니다.
     * @param size 가져올 게시글의 개수
     * @param principal 인증 정보
     * @return 게시글 리스트
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @GetMapping("/feed/top/part")
    public BaseResponse<?> getMyPartRecentPopularPosts(@RequestParam int size, Principal principal)
            throws BaseException {

        return new BaseResponse<>(postService.getMyPartPopularRecentPosts(size,
                memberService.getLoginMember(principal)));
    }

    /**
     * 로그인한 사용자가 작성한 글을 페이징하여 가져오는 메서드입니다.
     * @param pageNumber 페이지 번호 (0부터 시작)
     * @param pageSize 페이지 크기
     * @param principal 인증 정보
     * @return 게시글 리스트
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @GetMapping("/myinfo/postList")
    public BaseResponse<?> getMyPosts(@RequestParam int pageNumber, @RequestParam int pageSize, Principal principal)
            throws BaseException {

        return new BaseResponse<>(postService.getMyPosts(pageNumber, pageSize, memberService.getLoginMember(principal)));
    }

    /**
     * 1개의 게시글을 삭제하는 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 성공 여부
     * @throws BaseException 로그인한 사용자가 없거나, 타인이 게시글을 삭제하려 시도한 경우
     */
    @DeleteMapping
    public BaseResponse<?> deletePost(@RequestParam String postId, Principal principal) throws BaseException {

        postService.deletePost(postId, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 좋아요 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 성공 메시지
     * @throws BaseException 로그인한 사용자가 없는 경우
     */
    @PostMapping("/like")
    public BaseResponse<?> like(@RequestParam String postId, Principal principal) throws BaseException {

        postService.like(postId, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 좋아요 취소 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 성공 메시지
     * @throws BaseException 로그인한 사용자가 없는 경우
     */
    @DeleteMapping("/like")
    public BaseResponse<?> unlike(@RequestParam String postId, Principal principal) throws BaseException {

        postService.unlike(postId, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 세션 import 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 생성된 프롬프트의 id
     * @throws BaseException 로그인한 사용자가 없거나, 자신의 게시글을 import하려고 시도하는 경우
     */
    @PostMapping("/import")
    public BaseResponse<?> importPost(@RequestParam String postId, Principal principal) throws BaseException {

        return new BaseResponse<>(postService.importPost(postId, memberService.getLoginMember(principal)));
    }

    /**
     * 게시글 1개에 대해 스크랩하는 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 성공 여부
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @PostMapping("/scrap")
    public BaseResponse<?> scrapPost(@RequestParam String postId, Principal principal) throws BaseException {

        postService.addScrap(postId, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 스크랩 1개를 삭제하는 메서드입니다.
     * @param postId 게시글 id
     * @param principal 인증 정보
     * @return 성공 여부
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @DeleteMapping("/scrap")
    public BaseResponse<?> deleteScrap(@RequestParam String postId, Principal principal) throws BaseException {

        postService.deleteScrap(postId, memberService.getLoginMember(principal));
        return new BaseResponse<>(SUCCESS);
    }

    /**
     * 사용자의 스크랩 리스트를 조회하는 메서드입니다.
     * @param pageNumber 페이지 번호
     * @param pageSize 페이지 크기
     * @param principal 인증 정보
     * @return 스크랩 리스트
     * @throws BaseException 로그인한 사용자가 없을 경우
     */
    @GetMapping("/scrap")
    public BaseResponse<?> getScrap(@RequestParam int pageNumber, @RequestParam int pageSize, Principal principal) throws BaseException {

        return new BaseResponse<>(postService.getScrapPosts(
                pageNumber,
                pageSize,
                memberService.getLoginMember(principal)));
    }
}
