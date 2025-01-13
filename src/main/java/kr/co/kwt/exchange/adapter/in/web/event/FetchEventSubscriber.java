package kr.co.kwt.exchange.adapter.in.web.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import kr.co.kwt.exchange.adapter.in.web.redis.RedisMessageService;
import kr.co.kwt.exchange.adapter.in.web.sse.SseEmitterService;
import kr.co.kwt.exchange.adapter.in.web.sse.SseSendRequest;
import kr.co.kwt.exchange.application.port.in.GetExchangeUseCase;
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
    private final GetExchangeUseCase getExchangeUseCase;

    @PostConstruct
    public void init() {
        redisMessageService.subscribe(this, FetchEventPublisher.CHANNEL);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            FetchEvent fetchEvent = objectMapper.readValue(message.getBody(), FetchEvent.class);
            sseEmitterService.send(new SseSendRequest<>(fetchEvent.getMessage()));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
