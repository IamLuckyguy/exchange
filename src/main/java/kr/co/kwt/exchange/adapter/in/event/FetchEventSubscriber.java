package kr.co.kwt.exchange.adapter.in.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FetchEventSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final SseEmitterService sseEmitterService;
    private final RedisMessageService redisMessageService;

    @PostConstruct
    public void init() {
        redisMessageService.subscribe(this, FetchEventPublisher.CHANNEL);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String body = objectMapper.readValue(message.getBody(), String.class);
            log.info("redis pub/sub channel: {}, body {}", channel, body);
            sseEmitterService.send();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
