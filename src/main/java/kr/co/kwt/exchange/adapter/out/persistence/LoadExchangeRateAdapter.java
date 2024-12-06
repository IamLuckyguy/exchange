package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateCustomRepository;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
class LoadExchangeRateAdapter implements LoadExchangeRatePort {

    private final ExchangeRateCustomRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRate> findByCurrencyCode(final String currencyCode) {
        return exchangeRateRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    public Flux<ExchangeRate> findAllByCurrencyCode(List<String> currencyCodes) {
        return exchangeRateRepository.findAllByCurrencyCode(currencyCodes);
    }

    @Override
    public Mono<Country> findCountryByCurrencyCode(String currencyCode) {
        return exchangeRateRepository
                .findByCurrencyCode(currencyCode)
                .map(ExchangeRate::getCountry);
    }

    /// DTO 조회 ///////////////////////////////// ////////////////////////////// //////////////////////////////

    @Override
    public Flux<GetExchangeRateResponse> getExchangeRates() {
        return exchangeRateRepository.getExchangeRates();
    }

    @Override
    public Mono<GetExchangeRateResponse> getExchangeRate(final GetExchangeRateRequest request) {
        return exchangeRateRepository.getExchangeRate(request);
    }

}
