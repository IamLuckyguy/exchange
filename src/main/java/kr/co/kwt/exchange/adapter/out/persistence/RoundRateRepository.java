package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.querydsl.RoundRateQueryDslRepository;
import kr.co.kwt.exchange.domain.RoundRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRateRepository extends JpaRepository<RoundRate, Long>, RoundRateQueryDslRepository {

}
