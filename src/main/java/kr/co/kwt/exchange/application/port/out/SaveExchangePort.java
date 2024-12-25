package kr.co.kwt.exchange.application.port.out;

import kr.co.kwt.exchange.domain.Exchange;

import java.util.List;

public interface SaveExchangePort {

    Exchange save(Exchange exchange);

    List<Exchange> saveAll(List<Exchange> exchanges);

    void batchInsert(List<Exchange> exchanges);
}
