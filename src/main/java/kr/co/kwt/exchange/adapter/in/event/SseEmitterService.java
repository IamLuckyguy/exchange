package kr.co.kwt.exchange.adapter.in.event;

import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.in.GetExchangeUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseEmitterService {

    private final SseEmitterProvider sseEmitterProvider;
    private final GetExchangeUseCase getExchangeUseCase;

    public void send() {
        log.info("send()");

        List<GetExchangeByRoundResult> exchangesWithLastRoundRate =
                getExchangeUseCase.getExchangesWithLastRoundRate();

        sseEmitterProvider
                .getSseEmitters()
                .forEach(sseEmitter -> doSend(exchangesWithLastRoundRate, sseEmitter));
    }

    private void doSend(List<GetExchangeByRoundResult> getExchangeByRoundResults, SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("Publish Fetch Event")
                    .data(getExchangeByRoundResults));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
