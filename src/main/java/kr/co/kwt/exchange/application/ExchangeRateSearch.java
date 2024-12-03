package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.SearchExchangeRateUseCase;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExchangeRateSearch implements SearchExchangeRateUseCase {

    private final LoadExchangeRatePort loadExchangeRatePort;

    public Mono<ExchangeRate> searchExchangeRate(final String currencyCode) {
        return loadExchangeRatePort.findByCurrencyCode(currencyCode);
    }

    @Override
    public Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(
            final SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest
    ) {
        return loadExchangeRatePort
                .searchExchangeRateWithCountry(searchExchangeRateWithCountryRequest);
    }

    @Override
    public Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry() {
        return loadExchangeRatePort
                .searchAllExchangeRateWithCountry();
    }

    @Override
    public Mono<SearchCountryResponse> searchCountry(SearchCountryRequest searchCountryRequest) {
        return loadExchangeRatePort
                .searchCountry(searchCountryRequest);
    }
}
