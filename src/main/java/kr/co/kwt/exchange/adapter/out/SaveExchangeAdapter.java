package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.ExchangeRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangePort;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveExchangeAdapter implements SaveExchangePort {

    private final ExchangeRepository exchangeRepository;

    @Override
    public Exchange save(Exchange exchange) {
        return exchangeRepository.save(exchange);
    }
}
