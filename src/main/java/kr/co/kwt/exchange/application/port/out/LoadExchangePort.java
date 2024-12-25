package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.domain.Exchange;

import java.util.List;
import java.util.Optional;

public interface LoadExchangePort {

    Optional<Exchange> findByCurrencyCode(String currencyCode);

    List<Exchange> findAll();

    List<GetExchangeResult> getExchanges();

    List<GetExchangeByRoundResult> getExchangesByRound(int start, int end);

    List<String> findAllCurrencyCodes(List<String> list);
}
