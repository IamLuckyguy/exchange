package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import kr.co.kwt.exchange.domain.RoundRate;

import java.util.List;

public interface RoundRateQueryDslRepository {
    void bulkInsert(List<RoundRate> roundRates);

    List<RoundRate> findLastRoundRates();
}
