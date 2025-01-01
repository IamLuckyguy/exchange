package kr.co.kwt.exchange.application.port.in;

import kr.co.kwt.exchange.application.port.dto.UpdateExchangeCommand;

public interface UpdateExchangeUseCase {

    void updateExchange(UpdateExchangeCommand updateExchangeCommand);
}
