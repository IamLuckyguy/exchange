package kr.co.kwt.exchange.adapter.in.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class SseEmitterProvider {

    private static final Long DEFAULT_TIME_OUT = 60L * 1000 * 60; // 60분
    private final List<SseEmitter> sseEmitters;

    public SseEmitterProvider() {
        sseEmitters = new CopyOnWriteArrayList<>(); // thread-safe List
    }

    public SseEmitter addSseEmitter() {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIME_OUT);
        sseEmitters.add(sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> {
            log.info("Timeout!!");
            sseEmitter.complete();
        });

        return sseEmitter;
    }
}
