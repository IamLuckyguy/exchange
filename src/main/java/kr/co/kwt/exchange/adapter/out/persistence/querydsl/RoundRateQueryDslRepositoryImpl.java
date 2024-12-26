package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import static kr.co.kwt.exchange.domain.QRoundRate.roundRate1;

@Repository
@RequiredArgsConstructor
public class RoundRateQueryDslRepositoryImpl implements RoundRateQueryDslRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JPAQueryFactory queryFactory;

    @Override
    public void bulkInsert(List<RoundRate> roundRates) {
        String query =
                "INSERT INTO round_rates(" +
                        "currency_code, " +
                        "round_rate, " +
                        "round, " +
                        "trend, " +
                        "trend_rate, " +
                        "live_status, " +
                        "market_status, " +
                        "fetched_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(query, roundRates, 100, ((ps, argument) -> {
            ps.setString(1, argument.getCurrencyCode());
            ps.setDouble(2, argument.getRoundRate());
            ps.setInt(3, argument.getRound());
            ps.setString(4, argument.getTrend());
            ps.setDouble(5, argument.getTrendRate());
            ps.setString(6, argument.getLiveStatus());
            ps.setString(7, argument.getMarketStatus());
            ps.setTimestamp(8, Timestamp.valueOf(argument.getFetchedAt()));
        }));
    }

    @Override
    public List<RoundRate> findLastRoundRates() {
        return queryFactory
                .selectFrom(roundRate1)
                .where(roundRate1.id.eq(
                        queryFactory
                                .select(roundRate1.id.max())
                                .from(roundRate1)
                                .groupBy(roundRate1.currencyCode)))
                .fetch();
    }
}
