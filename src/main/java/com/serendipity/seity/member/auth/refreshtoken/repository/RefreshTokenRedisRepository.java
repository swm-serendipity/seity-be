package com.serendipity.seity.member.auth.refreshtoken.repository;

import com.serendipity.seity.member.auth.refreshtoken.RefreshToken;
import org.springframework.data.repository.CrudRepository;

/**
 * refresh token 저장을 위한 redis repository 입니다.
 *
 * @author Min Ho CHO
 */
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
}
