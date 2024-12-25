package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;

import java.util.List;

public interface GetExchangeUseCase {

    List<GetExchangeResult> getExchanges();

    List<GetExchangeByRoundResult> getExchangesByRound(int start, int end);

}
