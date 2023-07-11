package com.serendipity.seity.member.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.member.MemberPart;
import com.serendipity.seity.member.auth.refreshtoken.RefreshToken;
import com.serendipity.seity.member.auth.refreshtoken.repository.RefreshTokenRedisRepository;
import com.serendipity.seity.member.dto.*;
import com.serendipity.seity.member.auth.provider.JwtTokenProvider;
import com.serendipity.seity.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.serendipity.seity.common.response.BaseResponseStatus.INVALID_REFRESH_TOKEN;
import static com.serendipity.seity.member.Member.createMember;

/**
 * member service 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Service
@Transactional
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
    public LoginResponse login(LoginRequest request) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getLoginId(), request.getPassword());

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        // 4. refresh token을 redis에 저장
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(authentication.getName())
                .authorities(authentication.getAuthorities())
                .refreshToken(tokenDto.getRefreshToken())
                .build());

        return new LoginResponse(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    /**
     * 회원가입 메서드입니다.
     * @param request 회원가입 request 객체
     * @return 회원가입 response 객체
     */
    public SignUpResponse signUp(SignUpRequest request) throws BaseException {

        return new SignUpResponse(memberRepository.save(createMember(passwordEncoder.encode(request.getPassword()),
                request.getName(), request.getLoginId(), request.getEmail(), request.getBirthDate(),
                MemberPart.getPart(request.getPart()))).getId());

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
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // 3. 저장된 refresh token 찾기
            RefreshToken refreshToken = refreshTokenRedisRepository.findByRefreshToken(token);

            if (refreshToken != null) {

                // 4. token 재발급
                TokenDto tokenDto = jwtTokenProvider.generateToken(refreshToken.getId(), refreshToken.getAuthorities());

                // 5. redis update
                refreshTokenRedisRepository.save(RefreshToken.builder()
                                .id(refreshToken.getId())
                                .authorities(refreshToken.getAuthorities())
                                .refreshToken(tokenDto.getRefreshToken())
                                .build());

                return new LoginResponse(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
            }
        }

        throw new BaseException(INVALID_REFRESH_TOKEN);
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
