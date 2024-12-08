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

    // TODO : 벌크성 쿼리로 수정이 필요
    @Override
    public Flux<ExchangeRate> bulkUpdateRateValues(
            @NonNull final List<String> currencyCodes,
            @NonNull final List<Double> rateValues
    ) {
        if (currencyCodes.size() != rateValues.size()) {
            return Flux.error(IllegalAccessError::new);
        }

        return exchangeRateRepository.bulkUpdateRateValues(currencyCodes, rateValues);
    }
}
