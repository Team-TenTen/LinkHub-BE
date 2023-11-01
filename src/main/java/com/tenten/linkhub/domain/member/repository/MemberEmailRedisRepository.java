package com.tenten.linkhub.domain.member.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

/**
 * key - verificationCode(인증번호)<br>
 * data - email(이메일)
 */
@Repository
public class MemberEmailRedisRepository {

    private static final String EMAIL_KEY_PREFIX = "Email:";
    private final StringRedisTemplate redisTemplate;

    public MemberEmailRedisRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getEmail(String code) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(getKey(code));
    }

    public void save(String code, String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(getKey(code), email);
    }

    public void saveExpire(String code, String email, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(getKey(code), email, expireDuration);
    }

    public void delete(String code) {
        redisTemplate.delete(getKey(code));
    }

    public void deleteAll() {
        Set<String> keys = redisTemplate.keys(EMAIL_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    private String getKey(String code) {
        return EMAIL_KEY_PREFIX + code;
    }
}
