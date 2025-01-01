package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.GetRoundResult;
import kr.co.kwt.exchange.application.port.in.GetRoundUseCase;
import kr.co.kwt.exchange.application.port.out.LoadRoundPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetRoundService implements GetRoundUseCase {

    private final LoadRoundPort loadRoundPort;

    @Override
    public GetRoundResult getRound() {
        return loadRoundPort
                .getLastRoundResult()
                .orElseThrow(ServerException::new);
    }
}
