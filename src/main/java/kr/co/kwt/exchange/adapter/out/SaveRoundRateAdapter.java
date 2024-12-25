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
    public List<RoundRate> saveAll(List<RoundRate> rounds) {
        return roundRateRepository.saveAll(rounds);
    }
}
