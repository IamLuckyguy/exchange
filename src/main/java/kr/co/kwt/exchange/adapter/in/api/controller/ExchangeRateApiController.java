package kr.co.kwt.exchange.adapter.in.api.controller;

import kr.co.kwt.exchange.adapter.in.api.dto.FetchExchangeRateResponse;
import kr.co.kwt.exchange.adapter.in.api.service.ExchangeRateApiService;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateResponse;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExchangeRateApiController {

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

    @PostMapping(value = "/exchange-rates/fetch")
    public Flux<FetchExchangeRateResponse> fetchExchangeRates(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam(
                    value = "fetchDate", required = false,
                    defaultValue = "#{T(java.time.LocalDate).now().toString()}"
            ) final LocalDate fetchDate
    ) {
        log.info("fetchExchangeRates 호출 성공 : {}", fetchDate);
        return exchangeRateApiService.fetchExchangeRates(fetchDate);
    }
}
