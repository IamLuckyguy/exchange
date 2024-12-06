package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.SearchCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchCountryResponse;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryRequest;
import kr.co.kwt.exchange.application.port.in.dto.SearchExchangeRateWithCountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class ExchangeRateCustomRepositoryImpl implements ExchangeRateCustomRepository {

    private final DatabaseClient databaseClient;

    @Override
    public Flux<SearchExchangeRateWithCountryResponse> searchAllExchangeRateWithCountry() {
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
                .map(resultMap -> new SearchExchangeRateWithCountryResponse(
                        (String) resultMap.get("country"),
                        (String) resultMap.get("country_flag"),
                        (String) resultMap.get("currency_code"),
                        (Double) resultMap.get("rate_value"),
                        (Integer) resultMap.get("unit_amount"),
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

    @SuppressWarnings({"", "JpaQueryApiInspection"})
    @Override
    public Mono<SearchExchangeRateWithCountryResponse> searchExchangeRateWithCountry(
            final SearchExchangeRateWithCountryRequest searchExchangeRateWithCountryRequest
    ) {
        return databaseClient.sql("select c.country_name as country," +
                        " c.country_flag as country_flag," +
                        " er.currency_code as currency_code," +
                        " er.rate_value as rate_value," +
                        " er.unit_amount as unit_amount," +
                        " er.updated_at as updated_at" +
                        " from exchange_rates as er" +
                        " join country as c on er.currency_code = c.currency_code" +
                        " where er.currency_code = :currencyCode")
                .bind("currencyCode", searchExchangeRateWithCountryRequest.getCurrencyCode())
                .fetch()
                .one()
                .map(resultMap -> new SearchExchangeRateWithCountryResponse(
                        (String) resultMap.get("country"),
                        (String) resultMap.get("country_flag"),
                        (String) resultMap.get("currency_code"),
                        (Double) resultMap.get("rate_value"),
                        (Integer) resultMap.get("unit_amount"),
                        (LocalDateTime) resultMap.get("updated_at")
                ));
    }

    @SuppressWarnings("JpaQueryApiInspection")
    @Override
    public Mono<SearchCountryResponse> searchCountry(final SearchCountryRequest searchCountryRequest) {
        return databaseClient.sql("select c.country_name as country_name," +
                        " c.country_flag as country_flag," +
                        " c.currency_code as currency_code" +
                        " from country as c" +
                        " where c.currency_code = :currencyCode")
                .bind("currencyCode", searchCountryRequest.getCurrencyCode())
                .fetch()
                .one()
                .map(resultMap -> new SearchCountryResponse(
                        (String) resultMap.get("currency_code"),
                        (String) resultMap.get("country_name"),
                        (String) resultMap.get("country_flag")
                ));
    }
}
