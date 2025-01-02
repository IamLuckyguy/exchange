package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import kr.co.kwt.exchange.domain.ClosingRate;

import java.util.List;

public interface ClosingRateQueryDslRepository {
    long bulkInsert(List<ClosingRate> lastRoundRates);
}
