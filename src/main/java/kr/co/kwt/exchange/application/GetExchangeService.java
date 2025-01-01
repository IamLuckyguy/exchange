package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.application.port.in.GetExchangeUseCase;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetExchangeService implements GetExchangeUseCase {

    private final LoadExchangePort loadExchangePort;

    @Override
    public List<GetExchangeResult> getExchanges() {
        return loadExchangePort.getExchanges();
    }

    @Override
    public List<GetExchangeByRoundResult> getExchangesByRound(@NonNull final Integer start, @NonNull final Integer end) {
        return loadExchangePort.getExchangesByRound(start, end);
    }
}
