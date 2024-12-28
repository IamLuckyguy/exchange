package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.Round;

public interface SaveRoundPort {

    Round save(Round round);

    Round saveAndFlush(Round round);
}
