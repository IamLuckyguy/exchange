package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.repositories.ExchangeRateDegreeCountRepository;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateDegreeCountResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRateDegreeCountPort;
import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LoadExchangeRateDegreeCountAdapter implements LoadExchangeRateDegreeCountPort {

    private final ExchangeRateDegreeCountRepository exchangeRateDegreeCountRepository;

    @Override
    public Mono<GetExchangeRateDegreeCountResponse> getExchangeRateDegreeCount() {
        return exchangeRateDegreeCountRepository.getExchangeRateDegreeCount();
    }

    @Override
    public Mono<ExchangeRateDegreeCount> findFirstByOrderByFetchedAtDesc() {
        return exchangeRateDegreeCountRepository.findFirstByOrderByFetchedAtDesc();
    }

}
