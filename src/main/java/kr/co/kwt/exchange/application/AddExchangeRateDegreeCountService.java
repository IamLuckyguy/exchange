package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.AddExchangeRateDegreeCountUseCase;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateDegreeCountResponse;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateDegreeCountPort;
import kr.co.kwt.exchange.domain.ExchangeRateDegreeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AddExchangeRateDegreeCountService implements AddExchangeRateDegreeCountUseCase {

    private final SaveExchangeRateDegreeCountPort saveExchangeRateDegreeCountPort;

    @Override
    public Mono<AddExchangeRateDegreeCountResponse> addExchangeRateDegreeCount(@NonNull final LocalDateTime localDateTime) {
        return saveExchangeRateDegreeCountPort
                .save(ExchangeRateDegreeCount.withoutId())
                .map(exchangeRateDegreeCount -> new AddExchangeRateDegreeCountResponse(
                        exchangeRateDegreeCount.getDegreeCount()));
    }
}
