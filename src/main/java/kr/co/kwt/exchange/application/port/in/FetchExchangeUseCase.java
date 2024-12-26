package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;
import kr.co.kwt.exchange.application.port.dto.FetchExchangeResult;

public interface FetchExchangeUseCase {

    FetchExchangeResult fetchExchange(FetchExchangeCommand fetchExchangeCommand);
}
