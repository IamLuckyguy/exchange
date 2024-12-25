package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.kwt.exchange.application.port.dto.GetRoundResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kr.co.kwt.exchange.domain.QRound.round1;

@Repository
@RequiredArgsConstructor
class RoundQueryDslRepositoryImpl implements RoundQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<GetRoundResult> getLastRoundResult() {
        return Optional.ofNullable(queryFactory
                .select(Projections.fields(
                        GetRoundResult.class,
                        round1.round,
                        round1.fetchedAt))
                .from(round1)
                .limit(1L)
                .fetchOne());
    }
}
