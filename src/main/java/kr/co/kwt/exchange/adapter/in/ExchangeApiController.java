package kr.co.kwt.exchange.adapter.in;


import kr.co.kwt.exchange.application.port.dto.*;
import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand.FetchRate;
import kr.co.kwt.exchange.application.port.in.*;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiClient;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiGetExchangeResult;
import kr.co.kwt.exchange.openapi.naver.NaverOpenApiRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/exchange-rates")
    public List<GetExchangeResult> getExchanges() {
        return loadExchangePort.getExchanges();
    }

    @PostMapping("/exchange-rates")
    public void addExchange(@RequestBody final AddExchangeCommand command) {
        addExchangeUseCase.addExchange(command);
    }

    @GetMapping("/exchange-rates/round")
    public List<GetExchangeByRoundResult> getExchangeRates(
            @RequestParam(name = "start") final Integer start,
            @RequestParam(
                    name = "end",
                    required = false,
                    defaultValue = "#{T(java.lang.Integer).MAX_VALUE}"
            ) final Integer end
    ) {
        return getExchangeUseCase.getExchangesByRound(start, end);
    }

    @PostMapping("/exchange-rates/fetch")
    public String fetchExchanges(
            @RequestParam(
                    name = "fetchDate",
                    required = false,
                    defaultValue = "#{T(java.time.LocalDateTime).now().toString()}"
            ) final LocalDateTime fetchDateTime
    ) {
        int remoteRound = openApiClient
                .getRound()
                .getRound();

        int localRound = getRoundUseCase
                .getRound()
                .getRound();

        // 회차 비교
        if (remoteRound == localRound) {
            return "이미 패치되었습니다.";
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

        fetchExchangeUseCase.fetchExchange(fetchExchangeCommand);
        return "패치에 성공하였습니다.";
    }

    @GetMapping("/round")
    public GetRoundResult getFinalRound() {
        return getRoundUseCase.getRound();
    }

    @PostMapping("/round")
    public Integer addRound() {
        return addRoundUseCase.addRound();
    }
}
