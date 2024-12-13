package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ExchangeRateCustomRepositoryImpl implements ExchangeRateCustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<GetExchangeRateResponse> getExchangeRate(final GetExchangeRateRequest request) {
        return databaseClient.sql("select c.country_name as country_name," +
                        " c.country_flag as country_flag," +
                        " er.currency_code as currency_code," +
                        " er.rate_value as rate_value," +
                        " er.unit_amount as unit_amount," +
                        " er.decimals as decimals," +
                        " er.fetched_at as fetched_at," +
                        " er.updated_at as updated_at" +
                        " from exchange_rates as er" +
                        " join country as c on er.currency_code = c.currency_code" +
                        " join exchange_rate_histories erh on c.currency_code = erh.currency_code" +
                        " where er.currency_code = :currencyCode" +
                        " order by erh.updated_at desc limit 365")
                .bind("currencyCode", request.getCurrencyCode())
                .fetch()
                .one()
                .map(resultMap -> new GetExchangeRateResponse(
                        (String) resultMap.get("country_name"),
                        (String) resultMap.get("country_flag"),
                        (String) resultMap.get("currency_code"),
                        null,
                        (Integer) resultMap.get("unit_amount"),
                        (Integer) resultMap.get("decimals"),
                        (LocalDateTime) resultMap.get("fetched_at"),
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

    @Override
    public Flux<GetExchangeRateResponse> getExchangeRates() {
        return databaseClient
                .sql("select c.country_name as country," +
                        " c.country_flag as country_flag," +
                        " er.currency_code as currency_code," +
                        " er.rate_value as rate_value," +
                        " er.unit_amount as unit_amount," +
                        " er.decimals as decimals," +
                        " er.fetched_at as fetched_at," +
                        " er.updated_at as updated_at " +
                        " from exchange_rates as er" +
                        " join country as c on er.currency_code = c.currency_code")
                .fetch()
                .all()
                .map(resultMap -> new GetExchangeRateResponse(
                        (String) resultMap.get("country"),
                        (String) resultMap.get("country_flag"),
                        (String) resultMap.get("currency_code"),
                        null,
                        (Integer) resultMap.get("unit_amount"),
                        (Integer) resultMap.get("decimals"),
                        (LocalDateTime) resultMap.get("fetched_at"),
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

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
