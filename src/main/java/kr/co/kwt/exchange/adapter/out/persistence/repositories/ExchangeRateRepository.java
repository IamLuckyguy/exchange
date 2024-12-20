package kr.co.kwt.exchange.adapter.out.persistence.repositories;

import kr.co.kwt.exchange.application.port.in.dto.GetExchangeRateResponse;
import kr.co.kwt.exchange.domain.ExchangeRate;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, Long>, ExchangeRateCustomRepository {

    Mono<ExchangeRate> findByCurrencyCode(String currencyCode);

    Flux<ExchangeRate> findAllByCurrencyCodeIn(List<String> currencyCode);

    @Query("select c.country_name as country_name," +
            " c.country_flag as country_flag," +
            " er.currency_code as currency_code," +
            " er.rate_value as rate_value," +
            " er.unit_amount as unit_amount," +
            " er.decimals as decimals," +
            " er.fetched_at as fetched_at," +
            " er.updated_at as updated_at " +
            " from exchange_rates as er" +
            " join country as c on er.currency_code = c.currency_code" +
            " where er.currency_code in ('KRW', 'JPY', 'USD', 'CNY', 'EUR')")
    Flux<GetExchangeRateResponse> getMajorExchangeRates();

    @Query("select c.country_name as country_name," +
            " c.country_flag as country_flag," +
            " er.currency_code as currency_code," +
            " er.rate_value as rate_value," +
            " er.unit_amount as unit_amount," +
            " er.decimals as decimals," +
            " er.fetched_at as fetched_at," +
            " er.updated_at as updated_at " +
            " from exchange_rates as er" +
            " join country as c on er.currency_code = c.currency_code")
    Flux<GetExchangeRateResponse> getExchangeRates();


    @Query("select c.country_name as country_name," +
            " c.country_flag as country_flag," +
            " er.currency_code as currency_code," +
            " er.rate_value as rate_value," +
            " er.unit_amount as unit_amount," +
            " er.decimals as decimals," +
            " er.fetched_at as fetched_at," +
            " er.updated_at as updated_at" +
            " from exchange_rates as er" +
            " join country as c on er.currency_code = c.currency_code" +
            " where er.currency_code = :currencyCode")
    Mono<GetExchangeRateResponse> getExchangeRate(final String currencyCode);

}