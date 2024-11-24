package kr.co.kwt.exchange.adpater.in.api;

import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class ExchangeRateApiController {

    private final SearchExchangeRateUseCase searchExchangeRateUseCase;
    private final UpdateRateUseCase updateExchangeRateUseCase;
    private final AddExchangeRateUseCase addExchangeRateUseCase;

    @GetMapping("/exchange-rates")
    public Flux<SearchExchangeRateResponse> getExchangeRates() {
        return searchExchangeRateUseCase.searchAllExchangeRate();
    }

    @GetMapping("/exchange-rates/{country}")
    public Mono<ServerResponse> getExchangeRates(
            @ModelAttribute final SearchExchangeRateRequest searchExchangeRateRequest
    ) {
        return ok().bodyValue(searchExchangeRateUseCase.searchExchangeRate(searchExchangeRateRequest));
    }

    @PostMapping("/exchange-rates")
    public Mono<AddExchangeRateResponse> addExchangeRate(
            @RequestBody final AddExchangeRateRequest addExchangeRateRequest
    ) {
        return addExchangeRateUseCase.addExchangeRate(addExchangeRateRequest);
    }

    @PostMapping("/exchange-rates/fetch")
    public Mono<ServerResponse> fetchExchangeRates() {
        return Mono.empty();
    }
}
