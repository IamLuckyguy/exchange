package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.ClosingRateRepository;
import kr.co.kwt.exchange.application.port.out.SaveClosingRatePort;
import kr.co.kwt.exchange.domain.ClosingRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveClosingRateAdapter implements SaveClosingRatePort {

    private final ClosingRateRepository closingRateRepository;

    @Override
    public List<ClosingRate> saveAll(List<ClosingRate> closingRates) {
        return closingRateRepository.saveAll(closingRates);
    }

}
