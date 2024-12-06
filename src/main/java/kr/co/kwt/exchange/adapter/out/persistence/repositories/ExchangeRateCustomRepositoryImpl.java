package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExchangeRateCustomRepositoryImpl implements ExchangeRateCustomRepository {

    private final DatabaseClient databaseClient;

    @SuppressWarnings({"", "JpaQueryApiInspection"})
    @Override
    public Mono<GetExchangeRateResponse> GetExchangeRate(final GetExchangeRateRequest request) {
        return databaseClient.sql("select c.country_name as country," +
                        " c.country_flag as country_flag," +
                        " er.currency_code as currency_code," +
                        " er.rate_value as rate_value," +
                        " er.unit_amount as unit_amount," +
                        " er.updated_at as updated_at," +
                        " from exchange_rates as er" +
                        " join country as c on er.currency_code = c.currency_code" +
                        " where er.currency_code = :currencyCode")
                .bind("currencyCode", request.getCurrencyCode())
                .fetch()
                .one()
                .map(resultMap -> new GetExchangeRateResponse(
                        (String) resultMap.get("country"),
                        (String) resultMap.get("country_flag"),
                        (String) resultMap.get("currency_code"),
                        (Double) resultMap.get("rate_value"),
                        (Integer) resultMap.get("unit_amount"),
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

    @Override
    public Flux<GetExchangeRateResponse> GetExchangeRates() {
        return databaseClient
                .sql("select c.country_name as country," +
                        " c.country_flag as country_flag," +
                        " er.currency_code as currency_code," +
                        " er.rate_value as rate_value," +
                        " er.unit_amount as unit_amount," +
                        " er.updated_at as updated_at " +
                        " from exchange_rates as er" +
                        " join country as c on er.currency_code = c.currency_code")
                .fetch()
                .all()
                .map(resultMap -> new GetExchangeRateResponse(
                        (String) resultMap.get("country"),
                        (String) resultMap.get("country_flag"),
                        (String) resultMap.get("currency_code"),
                        (Double) resultMap.get("rate_value"),
                        (Integer) resultMap.get("unit_amount"),
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

    @Override
    public Flux<ExchangeRate> findAllByCurrencyCode(List<String> currencyCodes) {
        return null;
    }

    @Override
    public Flux<ExchangeRate> bulkUpdateRateValues(List<String> currencyCodes, List<Double> rateValues) {
        return databaseClient.sql("insert into exchange_rates as er" +
                " (" +
                " rate_value" +
                " )" +
                " values" +
                " (" +
                " er." +
                " )"
                ).
    }
}
