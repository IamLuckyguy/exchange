package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExchangeRateSearch implements SearchExchangeRateUseCase {

    private final LoadExchangeRatePort loadExchangeRatePort;

    @Override
    public Mono<SearchExchangeRateResponse> searchExchangeRate(final SearchExchangeRateRequest searchExchangeRateRequest) {
        return loadExchangeRatePort
                .findByCountry(searchExchangeRateRequest.getCountry())
                .map(SearchExchangeRateResponse::of);
    }

    @Override
    public Flux<SearchExchangeRateResponse> searchAllExchangeRate() {
        return loadExchangeRatePort
                .findAll()
                .map(SearchExchangeRateResponse::of);
    }
}
