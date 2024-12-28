package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.RoundRepository;
import kr.co.kwt.exchange.application.port.out.SaveRoundPort;
import kr.co.kwt.exchange.domain.Round;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRoundAdapter implements SaveRoundPort {

    private final RoundRepository roundRepository;

    @Override
    public Round save(final Round round) {
        return roundRepository.save(round);
    }

    @Override
    public Round saveAndFlush(Round round) {
        return roundRepository.saveAndFlush(round);
    }
}
