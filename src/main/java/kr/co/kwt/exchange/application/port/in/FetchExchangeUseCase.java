package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;

public interface FetchExchangeUseCase {

    void fetchExchange(FetchExchangeCommand fetchExchangeCommand);
}
