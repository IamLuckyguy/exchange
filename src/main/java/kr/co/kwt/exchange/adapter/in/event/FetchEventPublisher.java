package kr.co.kwt.exchange.adapter.in.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchEventPublisher {

    public static final String CHANNEL = "exchange";
    private final RedisMessageService redisMessageService;

    public void publishEvent() {
        log.info("publishEvent()");
        redisMessageService.publish(CHANNEL, "FETCHED");
    }
}
