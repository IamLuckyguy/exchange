package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;
import kr.co.kwt.exchange.application.port.dto.FetchExchangeResult;
import kr.co.kwt.exchange.application.port.in.FetchExchangeUseCase;
import kr.co.kwt.exchange.application.port.out.*;
import kr.co.kwt.exchange.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FetchExchangeService implements FetchExchangeUseCase {

    private final LoadRoundPort loadRoundPort;
    private final SaveRoundPort saveRoundPort;
    private final LoadExchangePort loadExchangePort;
    private final LoadRoundRatePort loadRoundRatePort;
    private final SaveRoundRatePort saveRoundRatePort;
    private final SaveClosingRatePort saveClosingRatePort;

    /**
     * 하나은행 기준 날짜마다 대한 환율가를 고시한다.
     * 이때, 각각의 고시를 회차별로 나누어 구분한다.
     * 그리고 마지막 회차에 대한 환율가격을 종가라고 한다.
     */
    @Override
    @Transactional
    public FetchExchangeResult fetchExchange(@NonNull final FetchExchangeCommand command) {
        long updatedRoundRatesCount = 0;
        long updatedClosingRatesCount = 0;

        Map<String, Exchange> currencyCodeToExchangeMap = getCurrencyCodeToExchangeMap();
        Round round = getLastRound();

        if (command.getRound().equals(round.getRound())) {
            throw new AlreadyFetchedExeption();
        }
        else if (command.getRound() > round.getRound()) { // 다음날 신규 회차
            round = doUpdateRound(command, round);
            updatedRoundRatesCount = saveRoundRatePort.bulkInsert(
                    getRoundRateList(
                            command,
                            currencyCodeToExchangeMap,
                            round));
        }
        else {
            round = doUpdateRound(command, round);
            updatedClosingRatesCount = saveClosingRatePort.bulkInsert(getClosingRateList());
            updatedRoundRatesCount = saveRoundRatePort.bulkInsert(
                    getRoundRateList(
                            command,
                            currencyCodeToExchangeMap,
                            round));
        }

        return new FetchExchangeResult(updatedRoundRatesCount, updatedClosingRatesCount, round.getRound());
    }

    private Round getLastRound() {
        return loadRoundPort
                .findLastRound()
                .orElseThrow(ServerException::new);
    }

    private Round doUpdateRound(
            @NonNull final FetchExchangeCommand command,
            @NonNull final Round round
    ) {
        return saveRoundPort.saveAndFlush(round.updateRound(command.getRound(), command.getFetchedAt()));
    }

    private List<RoundRate> getRoundRateList(
            @NonNull final FetchExchangeCommand fetchExchangeCommand,
            @NonNull final Map<String, Exchange> currecyCodeToExchangeMap,
            @NonNull final Round round
    ) {
        return fetchExchangeCommand
                .getFetchRates()
                .stream()
                .filter(fetchRate -> Country.isValidCurrencyCode(fetchRate.getCurrencyCode()))
                .map(fetchRate -> fetchRate.toRoundRate(
                        currecyCodeToExchangeMap.get(fetchRate.getCurrencyCode()),
                        round,
                        fetchExchangeCommand.getFetchedAt()))
                .toList();
    }

    private List<ClosingRate> getClosingRateList() {
        return loadRoundRatePort
                .findLastRoundRates()
                .stream()
                .map(roundRate -> ClosingRate.withoutId(
                        roundRate.getExchange(),
                        roundRate.getRoundRate(),
                        roundRate.getFetchedAt()))
                .toList();
    }

    private Map<String, Exchange> getCurrencyCodeToExchangeMap() {
        return loadExchangePort.findAll()
                .stream()
                .collect(Collectors.toMap(Exchange::getCurrencyCode, Function.identity()));
    }
}
