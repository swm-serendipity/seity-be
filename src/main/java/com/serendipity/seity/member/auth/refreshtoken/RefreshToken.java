package com.serendipity.seity.member.auth.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * refresh token 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 604800)
public class RefreshToken {

    private String id;
    private Collection<? extends GrantedAuthority> authorities;

    @Indexed
    private String refreshToken;
}
