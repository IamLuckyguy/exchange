package kr.co.kwt.exchange.adapter.in.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisMessageService {

    private final RedisMessageListenerContainer container;
    private final RedisTemplate<String, Object> redisTemplate;

    public void subscribe(MessageListener messageListener, String channel) {
        container.addMessageListener(messageListener, ChannelTopic.of(channel));
    }

    // 이벤트 발행
    public void publish(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }

    // 구독 삭제
    public void removeSubscribe(MessageListener messageListener, String channel) {
        container.removeMessageListener(messageListener, ChannelTopic.of(channel));
    }
}
