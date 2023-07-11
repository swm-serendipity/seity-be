package com.serendipity.seity.member.service;

import com.serendipity.seity.common.exception.BaseException;
import com.serendipity.seity.member.MemberPart;
import com.serendipity.seity.member.MemberRole;
import com.serendipity.seity.member.dto.*;
import com.serendipity.seity.member.auth.provider.JwtTokenProvider;
import com.serendipity.seity.member.repository.MemberRepository;
import com.serendipity.seity.redis.repository.RedisTemplateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final RedisTemplateRepository redisTemplateRepository;

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
        redisTemplateRepository.storeString(request.getLoginId(), tokenDto.getRefreshToken());
        
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
                MemberRole.getRole(request.getMemberRole()), MemberPart.getPart(request.getPart()))).getId());

    }
}
