package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.domain.ClosingRate;
import kr.co.kwt.exchange.domain.Exchange;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Exchange> findAllByQueryDsl() {
        List<Exchange> exchanges = queryFactory
                .selectFrom(exchange)
                .fetch();

        Map<String, List<RoundRate>> roundRatesMap = getCurrencyCodeToRoundRatesMap();
        Map<String, List<ClosingRate>> closingRatesMap = getCurrencyCodeToClosingRatesMap();

        for (Exchange exchange : exchanges) {
            exchange.addAllDailyRoundRates(roundRatesMap
                    .getOrDefault(exchange.getCurrencyCode(), new ArrayList<>()));

            exchange.addAllYearlyClosingRates(closingRatesMap
                    .getOrDefault(exchange.getCurrencyCode(), new ArrayList<>()));
        }

        return exchanges;
    }

    private Map<String, List<RoundRate>> getCurrencyCodeToRoundRatesMap() {
        return queryFactory
                .selectFrom(roundRate1)
                .join(roundRate1.exchange, exchange).fetchJoin()
                .where(roundRate1.fetchedAt.after(LocalDate.now().atStartOfDay().minusDays(1)))
                .orderBy(roundRate1.fetchedAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(roundRate -> roundRate.getExchange().getCurrencyCode(), Collectors.toList()));
    }

    private Map<String, List<ClosingRate>> getCurrencyCodeToClosingRatesMap() {
        return queryFactory
                .selectFrom(closingRate1)
                .join(closingRate1.exchange, exchange).fetchJoin()
                .where(closingRate1.fetchedAt.after(LocalDate.now().atStartOfDay().minusYears(1)))
                .orderBy(closingRate1.fetchedAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(closingRate -> closingRate.getExchange().getCurrencyCode(), Collectors.toList()));
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
                .selectFrom(roundRate1)
                .where(roundRate1.fetchRound.between(start, end)
                        .and(roundRate1.fetchedAt.after(LocalDate.now().atStartOfDay())))
                .orderBy(roundRate1.fetchedAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(roundRate -> roundRate.getExchange().getCurrencyCode()))
                .entrySet()
                .stream()
                .map(entry -> GetExchangeByRoundResult.of(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Override
    public List<String> findAllCurrencyCodes(List<String> currencyCodes) {
        return queryFactory
                .select(exchange.currencyCode)
                .from(exchange)
                .where(exchange.currencyCode.in(currencyCodes))
                .fetch();
    }

    @Override
    public List<GetExchangeByRoundResult> getExchangesWithLastRoundRate() {
        return queryFactory
                .selectFrom(roundRate1)
                .orderBy(roundRate1.fetchedAt.desc())
                .limit(1)
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(roundRate -> roundRate.getExchange().getCurrencyCode()))
                .entrySet()
                .stream()
                .map(entry -> GetExchangeByRoundResult.of(entry.getKey(), entry.getValue()))
                .toList();
    }
}
