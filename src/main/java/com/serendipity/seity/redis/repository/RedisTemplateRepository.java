package com.serendipity.seity.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * redis <key, value> 저장을 위한 컴포넌트 클래스입니다.
 *
 * @author Min Ho CHO
 */
@Component
@RequiredArgsConstructor
public class RedisTemplateRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final ValueOperations<String, String> valueOperations;

    public void storeString(String key, String value) {
        valueOperations.set(key, value);
    }

    public String retrieveString(String key) {
        return valueOperations.get(key);
    }

    public void deleteString(String key) {
        redisTemplate.delete(key);
    }
}
