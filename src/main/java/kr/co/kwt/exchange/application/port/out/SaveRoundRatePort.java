package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.RoundRate;

import java.util.List;

public interface SaveRoundRatePort {

    List<RoundRate> saveAll(List<RoundRate> roundRates);
}
