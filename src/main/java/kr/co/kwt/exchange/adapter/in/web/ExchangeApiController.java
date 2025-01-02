package kr.co.kwt.exchange.adapter.in.web;


import kr.co.kwt.exchange.application.ServerException;
import kr.co.kwt.exchange.application.port.dto.*;
import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand.FetchRate;
import kr.co.kwt.exchange.application.port.in.*;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiGetExchangeResult;
import kr.co.kwt.exchange.openapi.naver.NaverOpenApiRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExchangeApiController {

    private final GetRoundUseCase getRoundUseCase;
    private final AddRoundUseCase addRoundUseCase;
    private final LoadExchangePort loadExchangePort;
    private final GetExchangeUseCase getExchangeUseCase;
    private final FetchExchangeUseCase fetchExchangeUseCase;
    private final AddExchangeUseCase addExchangeUseCase;
    private final OpenApiClient openApiClient;
    private final SseEmitterProvider sseEmitterProvider;

    @GetMapping("/exchange-rates")
    public ResponseEntity<List<GetExchangeResult>> getExchanges() {
        return ResponseEntity.ok(loadExchangePort.getExchanges());
    }

    @PostMapping("/exchange-rates")
    public ResponseEntity<Long> addExchange(@RequestBody final AddExchangeCommand command) {
        return ResponseEntity.ok(addExchangeUseCase.addExchange(command));
    }

    @GetMapping(value = "/exchange-rates/event/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribeExchangeRatesEvent() {
        SseEmitter sseEmitter = sseEmitterProvider.addSseEmitter();

        try {
            sseEmitter.send(SseEmitter.event()
                    .name("subscribe")
                    .data("success!"));
        }
        catch (IOException e) {
            throw new ServerException();
        }

        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping("/exchange-rates/round")
    public ResponseEntity<List<GetExchangeByRoundResult>> getExchangesByRound(
            @RequestParam(name = "start") final Integer start,
            @RequestParam(
                    name = "end",
                    required = false,
                    defaultValue = "#{T(java.lang.Integer).MAX_VALUE}"
            ) final Integer end
    ) {
        return ResponseEntity.ok(getExchangeUseCase.getExchangesByRound(start, end));
    }

    @PostMapping("/exchange-rates/fetch")
    public ResponseEntity<FetchExchangeResult> fetchExchanges(
            @RequestParam(
                    name = "fetchDate",
                    required = false,
                    defaultValue = "#{T(java.time.LocalDateTime).now().toString()}"
            ) final LocalDateTime fetchDateTime
    ) {
        return doFetchExchanges(fetchDateTime);
    }

    @GetMapping("/round")
    public ResponseEntity<GetRoundResult> getFinalRound() {
        return ResponseEntity.ok(getRoundUseCase.getRound());
    }

    @PostMapping("/round")
    public ResponseEntity<Integer> addRound() {
        return ResponseEntity.ok(addRoundUseCase.addRound());
    }

    private ResponseEntity<FetchExchangeResult> doFetchExchanges(LocalDateTime fetchDateTime) {
        int remoteRound = openApiClient
                .getRound()
                .getRound();

        int localRound = getRoundUseCase
                .getRound()
                .getRound();

        // 회차 비교
        if (remoteRound == localRound) {
            throw new InvalidFetchRequest();
        }

        // 환율 정보 조회
        List<FetchRate> fetchRates = openApiClient.getExchange(new NaverOpenApiRequest())
                .stream()
                .map(OpenApiGetExchangeResult::toFetchRate)
                .toList();

        // fetch
        FetchExchangeCommand fetchExchangeCommand = new FetchExchangeCommand(
                remoteRound,
                fetchDateTime,
                fetchRates);

        FetchExchangeResult fetchExchangeResult = fetchExchangeUseCase.fetchExchange(fetchExchangeCommand);
        publishFetchedEvent();

        return ResponseEntity.ok(fetchExchangeResult);
    }

    private void publishFetchedEvent() {
        List<GetExchangeByRoundResult> exchangeByRoundResults = loadExchangePort
                .getExchangesWithLastRoundRate();

        sseEmitterProvider
                .getSseEmitters()
                .forEach(sseEmitter -> doPublish(sseEmitter, exchangeByRoundResults));
    }

    private void doPublish(SseEmitter sseEmitter, List<GetExchangeByRoundResult> exchangeByRoundResults) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("Publish Fetch Event")
                    .data(exchangeByRoundResults));
        }
        catch (IOException e) {
            throw new ServerException(e);
        }
    }
}
