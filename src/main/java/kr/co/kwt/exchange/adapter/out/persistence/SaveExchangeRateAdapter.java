package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRatePort;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SaveExchangeRateAdapter implements SaveExchangeRatePort {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRate> save(ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }
}
