package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExchangeRateHistoryCustomRepositoryImpl implements ExchangeRateHistoryCustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<ExchangeRateHistory> bulkSave(final List<ExchangeRateHistory> histories) {
        String sql = """
                    INSERT INTO exchange_rate_histories (currency_code, rate_value, fetched_at, updated_at)
                    VALUES %s
                """.formatted(generateValues(histories));

        return databaseClient.sql(sql)
                .fetch()
                .rowsUpdated()
                .flux()
                .thenMany(Flux.fromIterable(histories));
    }

    private String generateValues(final List<ExchangeRateHistory> histories) {
        if (histories == null || histories.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (ExchangeRateHistory history : histories) {
            stringBuilder.append("(");
            stringBuilder.append("'");
            stringBuilder.append(history.getCurrencyCode());
            stringBuilder.append("'");
            stringBuilder.append(",");
            stringBuilder.append(history.getRateValue());
            stringBuilder.append(",");
            stringBuilder.append("'");
            stringBuilder.append(history.getFetchedAt());
            stringBuilder.append("'");
            stringBuilder.append(",");
            stringBuilder.append("'");
            stringBuilder.append(history.getUpdatedAt());
            stringBuilder.append("'");
            stringBuilder.append("),");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
