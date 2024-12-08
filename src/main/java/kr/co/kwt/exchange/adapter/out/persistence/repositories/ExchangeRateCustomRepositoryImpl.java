package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateRequest;
import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.Country;
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
    public Mono<ExchangeRate> save(final ExchangeRate exchangeRate) {
        return databaseClient.sql("insert into exchange_rates" +
                        "(" +
                        " currency_code, " +
                        " unit_amount," +
                        " decimals," +
                        " rate_value," +
                        " updated_at" +
                        ") " +
                        " values " +
                        "(" +
                        " :currencyCode," +
                        " :unitAmount," +
                        " :decimals," +
                        " :rateValue," +
                        " :updatedAt" +
                        ")"
                )
                .bind("currencyCode", exchangeRate.getCurrencyCode())
                .bind("unitAmount", exchangeRate.getUnitAmount())
                .bind("decimals", exchangeRate.getDecimals())
                .bind("rateValue", exchangeRate.getRateValue())
                .bind("updatedAt", exchangeRate.getUpdatedAt())
                .fetch()
                .rowsUpdated()
                .then(Mono.just(exchangeRate));
    }

    @Override
    public Mono<ExchangeRate> findByCurrencyCode(String currencyCode) {
        return databaseClient.sql("select er.id as id," +
                        " er.currency_code as currency_code," +
                        " er.unit_amount as unit_amount," +
                        " er.decimals as decimals," +
                        " er.rate_value as rate_value," +
                        " er.updated_at as updated_at," +
                        " c.country_name as country_name," +
                        " c.country_flag as country_flag" +
                        " from exchange_rates as er" +
                        " join country as c on er.currency_code = c.currency_code" +
                        " where c.currency_code = :currencyCode")
                .bind("currencyCode", currencyCode)
                .fetch()
                .one()
                .map(result -> {

                    ExchangeRate exchangeRate = ExchangeRate.withId(
                            (Long) result.get("id"),
                            (String) result.get("currency_code"),
                            new Country(
                                    (String) result.get("country_name"),
                                    (String) result.get("currency_code"),
                                    (String) result.get("country_flag")
                            ),
                            (Integer) result.get("unit_amount"),
                            (Integer) result.get("decimals"),
                            (Double) result.get("rate_value"),
                            (LocalDateTime) result.get("updated_at")
                    );

                    log.info("exchangeRate: {}", exchangeRate);
                    return exchangeRate;
                });
    }

    // TODO : batch 쿼리로 수정 필요!
    @Override
    public Flux<ExchangeRate> findAllByCurrencyCode(final List<String> currencyCodes) {
        return Flux
                .fromIterable(currencyCodes)
                .flatMap(this::findByCurrencyCode);
    }

    @Override
    public Mono<GetExchangeRateResponse> getExchangeRate(final GetExchangeRateRequest request) {
        return databaseClient.sql("select c.country_name as country_name," +
                        " c.country_flag as country_flag," +
                        " er.currency_code as currency_code," +
                        " er.rate_value as rate_value," +
                        " er.unit_amount as unit_amount," +
                        " er.decimals as decimals," +
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
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

    // TODO : 벌크성 쿼리로 수정이 필요
    @Override
    public Flux<ExchangeRate> bulkUpdateRateValues(List<String> currencyCodes, List<Double> rateValues) {
        return null;
    }
}
