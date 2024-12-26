package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import kr.co.kwt.exchange.domain.ClosingRate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClosingRateQueryDslRepositoryImpl implements ClosingRateQueryDslRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public long bulkInsert(List<ClosingRate> lastRoundRates) {
        String query = "INSERT INTO closing_rates(currency_code, closing_rate, fetched_at) VALUES (?, ?, ?)";

        int[][] updatedCounts = jdbcTemplate.batchUpdate(query, lastRoundRates, 100, ((ps, argument) -> {
            ps.setString(1, argument.getCurrencyCode());
            ps.setDouble(2, argument.getClosingRate());
            ps.setTimestamp(3, Timestamp.valueOf(argument.getFetchedAt()));
        }));

        return Arrays
                .stream(updatedCounts)
                .flatMapToInt(Arrays::stream)
                .count();
    }
}
