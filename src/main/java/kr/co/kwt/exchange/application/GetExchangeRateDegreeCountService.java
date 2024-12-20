package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.GetExchangeRateDegreeCountUseCase;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateDegreeCountResponse;
import kr.co.kwt.exchange.application.port.out.LoadExchangeRateDegreeCountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetExchangeRateDegreeCountService implements GetExchangeRateDegreeCountUseCase {

    private final LoadExchangeRateDegreeCountPort loadExchangeRateDegreeCountPort;

    @Override
    public Mono<GetExchangeRateDegreeCountResponse> getExchangeRateDegreeCount() {
        return loadExchangeRateDegreeCountPort.getExchangeRateDegreeCount();
    }
}
