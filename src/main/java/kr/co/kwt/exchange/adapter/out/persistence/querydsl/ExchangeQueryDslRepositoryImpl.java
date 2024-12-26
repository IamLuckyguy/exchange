package kr.co.kwt.exchange.adapter.out.persistence.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.kwt.exchange.application.port.dto.GetExchangeByRoundResult;
import kr.co.kwt.exchange.application.port.dto.GetExchangeResult;
import kr.co.kwt.exchange.domain.Exchange;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
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
        return queryFactory
                .selectFrom(exchange)
                .leftJoin(exchange.dailyRoundRates, roundRate1)
                .leftJoin(exchange.yearlyClosingRates, closingRate1).fetchJoin()
                .fetch();
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
                .where(roundRate1.round.between(start, end)
                        .and(roundRate1.fetchedAt.after(LocalDate.now().atStartOfDay())))
                .orderBy(roundRate1.fetchedAt.desc())
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(RoundRate::getCurrencyCode))
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
}
