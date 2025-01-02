package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.in.AddRoundUseCase;
import kr.co.kwt.exchange.application.port.out.LoadRoundPort;
import kr.co.kwt.exchange.application.port.out.SaveRoundPort;
import kr.co.kwt.exchange.domain.Round;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AddRoundService implements AddRoundUseCase {

    private final LoadRoundPort loadRoundPort;
    private final SaveRoundPort saveRoundPort;

    @Override
    @Transactional
    public Integer addRound() {
        validateExistsRound();
        Round round = saveRoundPort.save(Round.withoutId(0, LocalDateTime.now()));
        return round.getRound();
    }

    private void validateExistsRound() {
        if (loadRoundPort
                .findLastRound()
                .isPresent()
        ) {
            throw new AlreadyExistsRoundException();
        }
    }
}
