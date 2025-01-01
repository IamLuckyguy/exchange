package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.RoundRate;

import java.util.List;

public interface LoadRoundRatePort {

    List<RoundRate> findLastRoundRates();
}
