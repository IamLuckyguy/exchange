package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.querydsl.ExchangeQueryDslRepository;
import kr.co.kwt.exchange.domain.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Long>, ExchangeQueryDslRepository {
    Optional<Exchange> findByCurrencyCode(String currencyCode);

//    Optional<Exchange> findByCurrencyCode(String currencyCode);
//
//    List<GetExchangeResult> getExchanges();
//
//    List<GetExchangeResult> getExchangesByRound(int start, int end);
}
