package kr.co.kwt.exchange.config;

import kr.co.kwt.exchange.config.kms.KmsRedisSecretValue;
import kr.co.kwt.exchange.config.kms.KmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final KmsService kmsService;
    private final ObjectMapper objectMapper;

    @Bean
    @Profile(value = {"local", "dev", "test"})
    public RedisConnectionFactory standaloneRedisConnectionFactory() {
        KmsRedisSecretValue secretValue = kmsService.getRedisSecretValue();

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(secretValue.getHost());
        configuration.setPort(secretValue.getPort());
        configuration.setPassword(secretValue.getPassword());

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Profile(value = "prod")
    public RedisConnectionFactory clusterRedisConnectionFactory() {
        KmsRedisSecretValue secretValue = kmsService.getRedisSecretValue();

        RedisSentinelConfiguration configuration = new RedisSentinelConfiguration();
        configuration.setMaster(secretValue.getMaster());
        configuration.setPassword(secretValue.getPassword());
        configuration.setSentinels(secretValue
                .getNodes()
                .stream()
                .map(node -> {
                    String[] parts = node.split(":");
                    return new RedisNode(parts[0], Integer.parseInt(parts[1]));
                })
                .toList());
        configuration.setSentinelPassword(secretValue.getPassword());

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // Value Serializer 설정
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(objectMapper);

        // Key와 Value의 직렬화 방식 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }
}
