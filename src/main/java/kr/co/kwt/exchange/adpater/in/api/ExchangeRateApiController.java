package kr.co.kwt.exchange.adpater.in.api;

import kr.co.kwt.exchange.application.port.in.AddExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.UpdateRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
class ExchangeRateApiController {

    private final SearchExchangeRateUseCase searchExchangeRateUseCase;
    private final UpdateRateUseCase updateExchangeRateUseCase;
    private final AddExchangeRateUseCase addExchangeRateUseCase;
    private final ExchangeRateFetcher exchangeRateFetcher;

    @GetMapping("/exchange-rates")
    public Flux<SearchExchangeRateResponse> getExchangeRates() {
        return searchExchangeRateUseCase.searchAllExchangeRate();
    }

    @GetMapping("/exchange-rates/{country}")
    public Mono<SearchExchangeRateResponse> getExchangeRates(
            @ModelAttribute final SearchExchangeRateRequest searchExchangeRateRequest
    ) {
        return searchExchangeRateUseCase.searchExchangeRate(searchExchangeRateRequest);
    }

    @PostMapping("/exchange-rates")
    public Mono<AddExchangeRateResponse> addExchangeRate(
            @RequestBody final AddExchangeRateRequest addExchangeRateRequest
    ) {
        return addExchangeRateUseCase.addExchangeRate(addExchangeRateRequest);
    }

    @PostMapping("/exchange-rates/fetch")
    public Flux<UpdateRateResponse> fetchExchangeRates(
            @RequestBody final FetchExchangeRateRequest fetchExchangeRateRequest
    ) {
        return updateExchangeRateUseCase.updateRates(exchangeRateFetcher
                .fetchAndSaveExchangeRates(fetchExchangeRateRequest.getSearchDate())
                .map(fetchExchangeRateResponse -> new UpdateRateRequest(
                        fetchExchangeRateResponse.getCurNm(),
                        fetchExchangeRateResponse.getCurUnit(),
                        Double.parseDouble(fetchExchangeRateResponse.getTtb().replace(",", "")))));
    }
}
