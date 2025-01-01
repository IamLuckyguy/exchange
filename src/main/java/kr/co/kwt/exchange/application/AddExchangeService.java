package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.AddExchangeCommand;
import kr.co.kwt.exchange.application.port.in.AddExchangeUseCase;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import kr.co.kwt.exchange.application.port.out.SaveExchangePort;
import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddExchangeService implements AddExchangeUseCase {

    private final LoadExchangePort loadExchangePort;
    private final SaveExchangePort saveExchangePort;

    @Override
    @Transactional
    public Long addExchange(@NonNull final AddExchangeCommand addExchangeCommand) {
        validateExistsExchangeBy(addExchangeCommand.getCurrencyCode());

        Exchange exchange = saveExchangePort.save(Exchange.withoutId(
                addExchangeCommand.getCurrencyCode(),
                Country.of(addExchangeCommand.getCurrencyCode()),
                addExchangeCommand.getUnit(),
                addExchangeCommand.getDecimals()));

        return exchange.getId();
    }

    private void validateExistsExchangeBy(@NonNull final String currencyCode) {
        if (loadExchangePort
                .findByCurrencyCode(currencyCode)
                .isPresent()
        ) {
            throw new AlreadyExistsExchangeException();
        }
    }
}
