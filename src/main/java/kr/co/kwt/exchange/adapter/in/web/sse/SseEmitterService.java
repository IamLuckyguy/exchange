package kr.co.kwt.exchange.adapter.in.web.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final SseEmitterProvider sseEmitterProvider;

    public void send(final SseSendRequest<?> sseSendRequest) {
        sseEmitterProvider
                .getSseEmitters()
                .forEach(sseEmitter -> doSend(sseSendRequest, sseEmitter));
    }

    private void doSend(final SseSendRequest<?> sseSendRequest, final SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter
                    .event()
                    .name("Publish Fetch Event")
                    .data(sseSendRequest.getBody()));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
