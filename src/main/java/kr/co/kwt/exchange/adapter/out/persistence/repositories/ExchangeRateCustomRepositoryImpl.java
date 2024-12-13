package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ExchangeRateCustomRepositoryImpl implements ExchangeRateCustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<ExchangeRate> bulkSave(final List<ExchangeRate> exchangeRates) {
        String sql = """
                    INSERT INTO exchange_rates (id, currency_code, rate_value, fetched_at)
                    VALUES %s
                    ON DUPLICATE KEY UPDATE
                        rate_value = VALUES(rate_value),
                        fetched_at = VALUES(fetched_at)
                """.formatted(generateValuesString(exchangeRates));

        // SQL 실행
        return databaseClient.sql(sql)
                .fetch()
                .rowsUpdated()
                .flux()
                .thenMany(Flux.fromIterable(exchangeRates));
    }

    private String generateValuesString(final List<ExchangeRate> exchangeRates) {
        if (exchangeRates == null || exchangeRates.isEmpty()) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (ExchangeRate exchangeRate : exchangeRates) {
            stringBuilder.append("(");
            stringBuilder.append(exchangeRate.getId());
            stringBuilder.append(",");
            stringBuilder.append("'").append(exchangeRate.getCurrencyCode()).append("'");
            stringBuilder.append(",");
            stringBuilder.append(exchangeRate.getRateValue());
            stringBuilder.append(",");
            stringBuilder.append("'").append(exchangeRate.getFetchedAt()).append("'");
            stringBuilder.append("),");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
