package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.GetExchangeRateRoundUseCase;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRoundResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRateRoundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetExchangeRateRoundService implements GetExchangeRateRoundUseCase {

    private final LoadExchangeRateRoundPort loadExchangeRateRoundPort;

    @Override
    public Mono<GetExchangeRateRoundResponse> getExchangeRateRound() {
        return loadExchangeRateRoundPort.getExchangeRateRound();
    }
}
