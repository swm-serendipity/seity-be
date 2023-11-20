package com.serendipity.seity.member.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.member.Member;
import com.serendipity.seity.member.MemberPart;
import com.serendipity.seity.member.MemberRole;
import com.serendipity.seity.member.auth.refreshtoken.RefreshToken;
import com.serendipity.seity.member.auth.refreshtoken.repository.RefreshTokenRedisRepository;
import com.serendipity.seity.member.dto.*;
import com.serendipity.seity.member.auth.provider.JwtTokenProvider;
import com.serendipity.seity.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.serendipity.seity.common.response.BaseResponseStatus.*;
import static com.serendipity.seity.member.Member.createMember;

/**
 * member service 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    /**
     * 로그인 메서드입니다.
     * @param request 로그인 정보
     * @return 토큰 생성 정보
     */
    public LoginResponse login(LoginRequest request) throws BaseException {

        Member findMember = memberRepository.findByLoginId(request.getLoginId()).orElseThrow(
                () -> new BaseException(INVALID_LOGIN_ID_OR_PASSWORD));

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (AuthenticationException e) {

            throw new BaseException(INVALID_LOGIN_ID_OR_PASSWORD);
        }

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // 4. refresh token을 redis에 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .authorities(authentication.getAuthorities())
                .userId(findMember.getId())
                .refreshToken(tokenDto.getRefreshToken())
                .build());

        return new LoginResponse(
                tokenDto.getAccessToken(),
                tokenDto.getRefreshToken(),
                findMember.getRoles());
    }

    /**
     * 회원가입 메서드입니다.
     * @param request 회원가입 request 객체
     * @return 회원가입 response 객체
     */
    public SignUpResponse signUp(SignUpRequest request) throws BaseException {

        if (memberRepository.findByLoginId(request.getLoginId()).isPresent()) {

            throw new BaseException(ALREADY_LOGIN_ID_EXIST);
        }

        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new BaseException(ALREADY_EMAIL_EXIST);
        }

        return new SignUpResponse(memberRepository.save(createMember(passwordEncoder.encode(request.getPassword()),
                request.getName(), request.getLoginId(), request.getEmail(), request.getBirthDate(),
                MemberPart.of(request.getPart()), MemberRole.of(request.getMemberRole()))).getId());

    }

    /**
     * 토큰 재발급 메서드입니다.
     *
     * @return 재발급된 토큰 정보
     */
    public LoginResponse reissueToken(HttpServletRequest request) throws BaseException {

        // 1. request header 에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 2. 토큰 유효성 검사
        // TODO: 여기서 문제 발생
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 3. 저장된 refresh token 찾기
            RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);

            if (refreshToken != null) {

                Member findMember =
                        memberRepository.findById(refreshToken.getUserId()).orElseThrow(
                                () -> new BaseException(INVALID_REFRESH_TOKEN_USER_ID_EXCEPTION));

                log.info("[refresh token 정보]");
                log.info("사용자 로그인 id: {}", findMember.getLoginId());

                // 4. token 재발급
                TokenDto tokenDto = jwtTokenProvider.generateToken(refreshToken.getId(), refreshToken.getAuthorities());

                // 5. redis update
                RefreshToken newToken = refreshTokenRedisRepository.save(RefreshToken.builder()
                        .id(refreshToken.getId())
                        .authorities(refreshToken.getAuthorities())
                        .userId(refreshToken.getUserId())
                        .refreshToken(tokenDto.getRefreshToken())
                        .build());

                return new LoginResponse(
                        tokenDto.getAccessToken(),
                        tokenDto.getRefreshToken(),
                        findMember.getRoles());
            }
        }

        throw new BaseException(INVALID_REFRESH_TOKEN);
    }

    /**
     * 현재 로그인한 사용자 정보를 불러오는 메서드입니다.
     * @param principal 현재 인증 정보
     * @return 로그인한 사용자
     */
    public Member getLoginMember(Principal principal) throws BaseException {

        return memberRepository.findByLoginId(principal.getName())
                .orElseThrow(() -> new BaseException(NO_LOGIN_USER));
    }

    /**
     * 사용자 멘션을 위해 모든 "USER"의 권한을 가진 사용자를 조회하는 메서드입니다.
     * @return 사용자 리스트
     */
    public List<MentionMemberResponse> getAllMemberForMention() {

        List<Member> findMembers = memberRepository.findByRolesContaining("USER");
        List<MentionMemberResponse> result = new ArrayList<>();

        for (Member member : findMembers) {

            result.add(MentionMemberResponse.of(member));
        }
        return result;
    }

    /**
     * 로그아웃 시 refresh token을 삭제하는 메서드입니다.
     * @param refreshToken refresh token
     */
    public void deleteRefreshToken(String refreshToken) {

        RefreshToken findToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken);

        if (findToken != null) {
            refreshTokenRedisRepository.delete(findToken);
        }
    }

    /**
     * 사용자 페이징 메서드입니다.
     * @param pageNumber 페이지 크기
     * @param pageSize 페이지 번호
     * @return 페이징 결과
     */
    public MemberPagingResponse getPagingMember(int pageNumber, int pageSize) {

        Pageable pageable =
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "createTime"));

        int totalMemberNumber = memberRepository.countByRolesContaining("USER");

        return pagingMembers(
                memberRepository.findByRolesContaining("USER", pageable),
                totalMemberNumber,
                (totalMemberNumber - 1) / pageSize + 1,
                pageNumber);
    }

    /**
     * 사용자 정보 수정 메서드입니다.
     * @param request 수정 정보
     */
    public void updateMember(UpdateMemberRequest request) throws BaseException {

        Member findMember = memberRepository.findById(request.getId()).orElseThrow(
                () -> new BaseException(INVALID_MEMBER_ID_UPDATE_EXCEPTION));

        findMember.update(request.getName(), MemberPart.ofForUpdate(request.getPart()), MemberRole.ofForUpdate(request.getRole()));
        memberRepository.save(findMember);
    }

    private MemberPagingResponse pagingMembers(List<Member> members, int totalMemberNumber, int totalPages, int page) {

        List<MultipleMemberResponse> result = new ArrayList<>();
        for (Member member : members) {

            result.add(MultipleMemberResponse.of(member));
        }

        return new MemberPagingResponse(totalPages, totalMemberNumber, page, result);
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
