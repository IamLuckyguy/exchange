package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.dto.AddExchangeCommand;

public interface AddExchangeUseCase {

    Long addExchange(AddExchangeCommand addExchangeCommand);
}
