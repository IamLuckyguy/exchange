package kr.co.kwt.exchange.adapter.in.api.controller;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateRequest;
import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.in.api.service.ExchangeRateApiService;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class ExchangeRateApiController {

    private final ExchangeRateApiService exchangeRateApiService;

    @GetMapping("/exchange-rates")
    public Flux<GetExchangeRateResponse> searchAllExchangeRateWithCountry() {
        return exchangeRateApiService.searchAllExchangeRateWithCountry();
    }

    @GetMapping("/exchange-rates/{currencyCode}")
    public Mono<GetExchangeRateResponse> searchExchangeRateWithCountry(
            @ModelAttribute final GetExchangeRateRequest getExchangeRateRequest
    ) {
        return exchangeRateApiService.searchExchangeRateWithCountry(getExchangeRateRequest);
    }

    @PostMapping("/exchange-rates")
    public Mono<AddExchangeRateResponse> addExchangeRate(
            @RequestBody final AddExchangeRateRequest addExchangeRateRequest
    ) {
        return exchangeRateApiService.addExchangeRate(addExchangeRateRequest);
    }

    @PostMapping("/exchange-rates/fetch")
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(
            @RequestBody final FetchExchangeRateRequest fetchExchangeRateRequest
    ) {
        return exchangeRateApiService.fetchExchangeRates(fetchExchangeRateRequest);
    }
}
