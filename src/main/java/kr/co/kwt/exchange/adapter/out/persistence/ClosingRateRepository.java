package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.querydsl.ClosingRateQueryDslRepository;
import kr.co.kwt.exchange.domain.ClosingRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClosingRateRepository extends JpaRepository<ClosingRate, Long>, ClosingRateQueryDslRepository {

}
