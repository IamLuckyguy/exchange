package kr.co.kwt.exchange.adapter.out.persistence;

import kr.co.kwt.exchange.adapter.out.persistence.querydsl.RoundQueryDslRepository;
import kr.co.kwt.exchange.domain.Round;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundRepository extends JpaRepository<Round, Long>, RoundQueryDslRepository {
}
