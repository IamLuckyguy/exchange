package kr.co.kwt.exchange.adpater.out.persistence;

import kr.co.kwt.exchange.adpater.out.persistence.repositories.ExchangeRateRepository;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoadExchangeRateAdapter implements LoadExchangeRatePort {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRate> findByCountry(String country) {
        return exchangeRateRepository.findByCountry(country);
    }

    @Override
    public Flux<ExchangeRate> findAll() {
        return exchangeRateRepository.findAll();
    }
}
