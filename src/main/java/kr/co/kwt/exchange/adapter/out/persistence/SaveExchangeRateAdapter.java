package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
class SaveExchangeRateAdapter implements SaveExchangeRatePort {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRate> save(@NonNull final ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    @Override
    public Flux<ExchangeRate> saveAll(@NonNull final Flux<ExchangeRate> exchangeRates) {
        return exchangeRateRepository.saveAll(exchangeRates);
    }

    @Override
    public Flux<ExchangeRate> bulkSave(@NonNull final List<ExchangeRate> exchangeRates) {
        return exchangeRateRepository.bulkSave(exchangeRates);
    }

}
