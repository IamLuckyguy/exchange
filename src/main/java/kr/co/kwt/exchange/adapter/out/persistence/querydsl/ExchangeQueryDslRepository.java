package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.domain.Exchange;

import java.util.List;

public interface ExchangeQueryDslRepository {

    List<Exchange> findAllByQueryDsl();

    List<GetExchangeResult> getExchanges();

    List<GetExchangeByRoundResult> getExchangesByRound(int start, int end);

    List<String> findAllCurrencyCodes(List<String> currencyCodes);

    List<GetExchangeByRoundResult> getExchangesWithLastRoundRate();
}
