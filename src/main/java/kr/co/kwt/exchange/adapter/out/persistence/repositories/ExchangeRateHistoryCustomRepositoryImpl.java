package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.domain.ExchangeRateHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ExchangeRateHistoryCustomRepositoryImpl implements ExchangeRateHistoryCustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<ExchangeRateHistory> find() {
        return null;
    }

    @Override
    public Mono<ExchangeRateHistory> save(final ExchangeRateHistory history) {
        return databaseClient.sql("insert into exchange_rate_histories" +
                        "(" +
                        " currency_code," +
                        " rate_value," +
                        " updated_at" +
                        ") " +
                        " values " +
                        "(" +
                        " :currencyCode," +
                        " :rateValue," +
                        " :updatedAt" +
                        ")")
                .bind("currencyCode", history.getCurrencyCode())
                .bind("rateValue", history.getRateValue())
                .bind("updatedAt", history.getUpdatedAt())
                .fetch()
                .rowsUpdated()
                .then(Mono.just(history));
    }
}
