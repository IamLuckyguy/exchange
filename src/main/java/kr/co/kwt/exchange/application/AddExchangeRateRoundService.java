package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.AddExchangeRateRoundUseCase;
import kr.co.kwt.exchange.application.port.in.dto.AddExchangeRateRoundResponse;
import kr.co.kwt.exchange.application.port.out.SaveExchangeRateRoundPort;
import kr.co.kwt.exchange.domain.ExchangeRateRound;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AddExchangeRateRoundService implements AddExchangeRateRoundUseCase {

    private final SaveExchangeRateRoundPort saveExchangeRateRoundPort;

    @Override
    public Mono<AddExchangeRateRoundResponse> addExchangeRateDegreeCount(@NonNull final LocalDateTime localDateTime) {
        return saveExchangeRateRoundPort
                .save(ExchangeRateRound.withoutId())
                .map(exchangeRateRound -> new AddExchangeRateRoundResponse(
                        exchangeRateRound.getDegreeCount()));
    }
}
