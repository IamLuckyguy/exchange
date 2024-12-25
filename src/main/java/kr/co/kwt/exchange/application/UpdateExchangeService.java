package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.UpdateExchangeCommand;
import kr.co.kwt.exchange.application.port.in.UpdateExchangeUseCase;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import kr.co.kwt.exchange.application.port.out.SaveExchangePort;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateExchangeService implements UpdateExchangeUseCase {

    private final LoadExchangePort loadExchangePort;
    private final SaveExchangePort saveExchangePort;

    @Override
    @Transactional
    public void updateExchange(@NonNull final UpdateExchangeCommand updateExchangeCommand) {
        Exchange exchange = loadExchangePort
                .findByCurrencyCode(updateExchangeCommand.getCurrencyCode())
                .orElseThrow(InvalidCurrencyCodeException::new);

        exchange.updateDecimals(updateExchangeCommand.getDecimals());
        exchange.updateUnit(updateExchangeCommand.getUnit());
        saveExchangePort.save(exchange);
    }
}
