package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.adapter.out.persistence.ExchangeRepository;
import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoadExchangeAdapter implements LoadExchangePort {

    private final ExchangeRepository exchangeRepository;

    @Override
    public Optional<Exchange> findByCurrencyCode(String currencyCode) {
        return exchangeRepository.findByCurrencyCode(currencyCode);
    }

    @Override
    public List<Exchange> findAll() {
        return exchangeRepository.findAllByQueryDsl();
    }

    @Override
    public List<GetExchangeResult> getExchanges() {
        return exchangeRepository.getExchanges();
    }

    @Override
    public List<GetExchangeByRoundResult> getExchangesByRound(int start, int end) {
        return exchangeRepository.getExchangesByRound(start, end);
    }

    @Override
    public List<String> findAllCurrencyCodes(List<String> currencyCodes) {
        return exchangeRepository.findAllCurrencyCodes(currencyCodes);
    }

    @Override
    public List<GetExchangeByRoundResult> getExchangesWithLastRoundRate() {
        return exchangeRepository.getExchangesWithLastRoundRate();
    }
}
