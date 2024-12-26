package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.application.port.dto.GetRoundRateResult;
import kr.co.kwt.exchange.domain.ClosingRate;
import kr.co.kwt.exchange.domain.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.kwt.exchange.domain.QClosingRate.closingRate1;
import static kr.co.kwt.exchange.domain.QExchange.exchange;
import static kr.co.kwt.exchange.domain.QRoundRate.roundRate1;

@Repository
@RequiredArgsConstructor
public class ExchangeQueryDslRepositoryImpl implements ExchangeQueryDslRepository {

    private final DataSource dataSource;
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Exchange> findAllByQueryDsl() {
        List<Exchange> exchangeWithRoundRates = getExchangeWithRoundRates();

        Map<String, List<ClosingRate>> yearlyClosingRatesMap =
                getCurrencyCodeYearlyClosingRatesMap(getExchangeWithClosingRates());

        for (Exchange exchangeWithRoundRate : exchangeWithRoundRates) {
            exchangeWithRoundRate.addAllYearlyClosingRates(yearlyClosingRatesMap
                    .getOrDefault(exchangeWithRoundRate.getCurrencyCode(), new ArrayList<>()));
        }

        return exchangeWithRoundRates;
    }

    @Override
    public List<GetExchangeResult> getExchanges() {
        return findAllByQueryDsl()
                .stream()
                .map(GetExchangeResult::of)
                .toList();
    }

    @Override
    public List<GetExchangeByRoundResult> getExchangesByRound(int start, int end) {
        return queryFactory
                .selectFrom(exchange)
                .join(exchange.dailyRoundRates, roundRate1)
                .where(roundRate1.round.between(start, end))
                .transform(GroupBy.groupBy(exchange.id).list(Projections.constructor(GetExchangeByRoundResult.class,
                        exchange.currencyCode,
                        exchange.country,
                        Projections.list(Projections.constructor(
                                GetRoundRateResult.class,
                                roundRate1.roundRate,
                                roundRate1.round,
                                roundRate1.trend,
                                roundRate1.trendRate,
                                roundRate1.liveStatus,
                                roundRate1.marketStatus,
                                roundRate1.fetchedAt
                        )))));
    }

    @Override
    public List<String> findAllCurrencyCodes(List<String> currencyCodes) {
        return queryFactory
                .select(exchange.currencyCode)
                .from(exchange)
                .where(exchange.currencyCode.in(currencyCodes))
                .fetch();
    }

    private List<Exchange> getExchangeWithRoundRates() {
        return queryFactory
                .selectFrom(exchange)
                .leftJoin(exchange.dailyRoundRates, roundRate1).fetchJoin()
                .orderBy(roundRate1.id.desc())
                .limit(2000)
                .fetch();
    }

    private List<Exchange> getExchangeWithClosingRates() {
        return queryFactory
                .selectFrom(exchange)
                .leftJoin(exchange.yearlyClosingRates, closingRate1).fetchJoin()
                .orderBy(closingRate1.id.desc())
                .limit(365)
                .fetch();
    }

    private Map<String, List<ClosingRate>> getCurrencyCodeYearlyClosingRatesMap(
            List<Exchange> exchangeWithClosingRates
    ) {
        return exchangeWithClosingRates
                .stream()
                .collect(Collectors.toMap(Exchange::getCurrencyCode, Exchange::getYearlyClosingRates));
    }
}
