package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.ExchangeRepository;
import kr.co.kwt.exchange.application.port.out.SaveExchangePort;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveExchangeAdapter implements SaveExchangePort {

    private final ExchangeRepository exchangeRepository;

    @Override
    public Exchange save(Exchange exchange) {
        return exchangeRepository.save(exchange);
    }

    @Override
    public List<Exchange> saveAll(List<Exchange> exchanges) {
        return exchangeRepository.saveAll(exchanges);
    }

    @Override
    public void batchInsert(List<Exchange> exchanges) {
        exchangeRepository.saveAllByQueryDsl(exchanges);
    }
}
