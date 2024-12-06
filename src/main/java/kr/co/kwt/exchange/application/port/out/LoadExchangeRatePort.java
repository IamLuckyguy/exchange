package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.ExchangeRate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface LoadExchangeRatePort {

    Mono<ExchangeRate> findByCurrencyCode(String currencyCode);

    Flux<ExchangeRate> findAllByCurrencyCode(List<String> currencyCodes);

    Mono<Country> findCountryByCurrencyCode(String currencyCode);

    /// DTO 조회 ///////////////////////////////// ////////////////////////////// //////////////////////////////

    Flux<GetExchangeRateResponse> getExchangeRates();

    Mono<GetExchangeRateResponse> getExchangeRate(GetExchangeRateRequest getExchangeRateRequest);
}
