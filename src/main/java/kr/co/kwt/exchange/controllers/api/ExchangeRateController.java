package kr.co.kwt.exchange.controllers.api;

import kr.co.kwt.exchange.models.ExchangeRate;
import kr.co.kwt.exchange.services.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/fetch/{date}")
    public Flux<ExchangeRate> fetchAndSaveExchangeRates(@PathVariable String date) {
        return exchangeRateService.fetchAndSaveExchangeRates(date);
    }

    @GetMapping("/{date}")
    public Flux<ExchangeRate> getExchangeRates(@PathVariable String date) {
        return exchangeRateService.getExchangeRates(date);
    }

    @GetMapping
    public Flux<ExchangeRate> getTodayExchangeRates() {
        return exchangeRateService.getTodayExchangeRates();
    }
}