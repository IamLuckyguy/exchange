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
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
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

    @Override
    public void saveAllByQueryDsl(List<Exchange> exchanges) {
        batchInsertRoundRates(exchanges);
        batchInsertClosingRates(exchanges);
        entityManager.clear();
    }

    private void batchInsertRoundRates(List<Exchange> exchanges) {
        List<RoundRate> roundRates = exchanges
                .stream()
                .map(Exchange::getDailyRoundRates)
                .flatMap(Collection::stream)
                .distinct()
                .toList();

        String sql = "INSERT INTO round_rates (currency_code, round_rate, round, trend, trend_rate, live_status, market_status, fetched_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // JDBC Batch 설정
            int batchSize = 50; // 배치 크기 설정
            int count = 0;

            for (RoundRate roundRate : roundRates) {
                preparedStatement.setString(1, roundRate.getCurrencyCode());
                preparedStatement.setBigDecimal(2, BigDecimal.valueOf(roundRate.getRoundRate()));
                preparedStatement.setInt(3, roundRate.getRound());
                preparedStatement.setString(4, roundRate.getTrend());
                preparedStatement.setBigDecimal(5, BigDecimal.valueOf(roundRate.getTrendRate()));
                preparedStatement.setString(6, roundRate.getLiveStatus());
                preparedStatement.setString(7, roundRate.getMarketStatus());
                preparedStatement.setTimestamp(8, Timestamp.valueOf(roundRate.getFetchedAt()));

                preparedStatement.addBatch(); // 배치에 추가
                count++;

                // 배치 실행
                if (count % batchSize == 0) {
                    preparedStatement.executeBatch();
                }
            }

            // 남은 데이터 실행
            preparedStatement.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Batch insert failed", e);
        }
    }

    private void batchInsertClosingRates(List<Exchange> exchanges) {
        // ClosingRate 리스트 평탄화
        List<ClosingRate> closingRates = exchanges
                .stream()
                .map(Exchange::getYearlyClosingRates)
                .flatMap(List::stream) // 중첩 리스트를 평탄화
                .distinct()
                .toList();

        // Native Query
        String sql = "INSERT INTO closing_rates (currency_code, closing_rate, fetched_at) VALUES (?, ?, ?)";

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            for (ClosingRate closingRate : closingRates) {
                ps.setString(1, closingRate.getCurrencyCode());
                ps.setBigDecimal(2, BigDecimal.valueOf(closingRate.getClosingRate()));
                ps.setTimestamp(3, Timestamp.valueOf(closingRate.getFetchedAt()));
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Batch insert failed", e);
        }
    }

//    private void batchInsertClosingRates(List<Exchange> exchanges) {
//        List<ClosingRate> closingRates = exchanges
//                .stream()
//                .map(Exchange::getYearlyClosingRates)
//                .flatMap(Collection::stream)
//                .toList();
//
//        JPAInsertClause insertClause = queryFactory.insert(closingRate1);
//        for (ClosingRate closingRate : closingRates) {
//            insertClause
//                    .set(closingRate1.currencyCode, closingRate.getCurrencyCode())
//                    .set(closingRate1.closingRate, closingRate.getClosingRate())
//                    .set(closingRate1.fetchedAt, closingRate.getFetchedAt());
//        }
//        insertClause.execute();
//    }

//    private void batchInsertRoundRates(List<Exchange> exchanges) {
//        List<RoundRate> roundRates = exchanges
//                .stream()
//                .map(Exchange::getDailyRoundRates)
//                .flatMap(Collection::stream)
//                .toList();
//
//        JPAInsertClause insertClause = queryFactory.insert(roundRate1);
//        for (RoundRate roundRate : roundRates) {
//            insertClause
//                    .set(roundRate1.currencyCode, roundRate.getCurrencyCode())
//                    .set(roundRate1.roundRate, roundRate.getRoundRate())
//                    .set(roundRate1.round, roundRate.getRound())
//                    .set(roundRate1.trend, roundRate.getTrend())
//                    .set(roundRate1.trendRate, roundRate.getTrendRate())
//                    .set(roundRate1.liveStatus, roundRate.getLiveStatus())
//                    .set(roundRate1.marketStatus, roundRate.getMarketStatus())
//                    .set(roundRate1.fetchedAt, roundRate.getFetchedAt());
//        }
//        insertClause.execute();
//    }

    private List<Exchange> getExchangeWithRoundRates() {
        return queryFactory
                .selectFrom(exchange)
                .leftJoin(exchange.dailyRoundRates, roundRate1).fetchJoin()
                .limit(2000)
                .fetch();
    }

    private List<Exchange> getExchangeWithClosingRates() {
        return queryFactory
                .selectFrom(exchange)
                .leftJoin(exchange.yearlyClosingRates, closingRate1).fetchJoin()
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
