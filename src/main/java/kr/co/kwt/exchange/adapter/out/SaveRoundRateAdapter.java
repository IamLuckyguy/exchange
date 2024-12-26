package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.RoundRateRepository;
import kr.co.kwt.exchange.application.port.out.SaveRoundRatePort;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveRoundRateAdapter implements SaveRoundRatePort {

    private final RoundRateRepository roundRateRepository;
    
    @Override
    public void bulkInsert(List<RoundRate> roundRates) {
        roundRateRepository.bulkInsert(roundRates);
    }
}
