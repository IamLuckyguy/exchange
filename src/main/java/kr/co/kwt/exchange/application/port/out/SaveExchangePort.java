package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.Exchange;

public interface SaveExchangePort {

    Exchange save(Exchange exchange);

}
