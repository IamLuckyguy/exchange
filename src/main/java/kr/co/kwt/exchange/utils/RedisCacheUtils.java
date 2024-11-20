package kr.co.kwt.exchange.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidari.lectureapi.common.constants.RedisKeys;
import com.kidari.lectureapi.common.models.dto.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
public class RedisCacheUtils {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheUtils(ReactiveRedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public <T> Mono<T> deserializeApiResponse(String cacheKey, Object cachedValue, TypeReference<T> typeReference) {
        log.info("Redis에 캐시된 데이터를 불러옵니다. Cache Key : {}", cacheKey);
        try {
            if (cachedValue instanceof String) {
                return Mono.just(objectMapper.readValue((String) cachedValue, typeReference));
            } else {
                return Mono.just(objectMapper.convertValue(cachedValue, typeReference));
            }
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Error deserializing API response", e));
        }
    }

    public <T> Mono<ApiResponseDto<T>> cacheResponse(String key, ApiResponseDto<T> response, Duration ttl) {
        log.info("Redis에 캐시를 저장합니다. Cache Key : {}", key);
        return redisTemplate.opsForValue().set(key, response, ttl).thenReturn(response);
    }

    public Mono<Void> invalidateUserRegistrationsCache(String userId) {
        return redisTemplate.keys(RedisKeys.getUserRegistrationsPattern(userId))
                .flatMap(redisTemplate.opsForValue()::delete)
                .then(redisTemplate.keys(RedisKeys.LECTURES_PAGE)
                        .flatMap(redisTemplate.opsForValue()::delete)
                        .then())
                .then(redisTemplate.keys(RedisKeys.POPULAR_LECTURES)
                        .flatMap(redisTemplate.opsForValue()::delete)
                        .then());
    }
}