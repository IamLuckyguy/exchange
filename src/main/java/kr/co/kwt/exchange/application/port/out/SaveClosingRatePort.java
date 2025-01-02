package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.ClosingRate;

import java.util.List;

public interface SaveClosingRatePort {

    long bulkInsert(List<ClosingRate> lastRoundRates);
}
