package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;
import kr.co.kwt.exchange.application.port.dto.FetchExchangeResult;
import kr.co.kwt.exchange.application.port.in.FetchExchangeUseCase;
import kr.co.kwt.exchange.application.port.out.LoadRoundPort;
import kr.co.kwt.exchange.application.port.out.LoadRoundRatePort;
import kr.co.kwt.exchange.application.port.out.SaveClosingRatePort;
import kr.co.kwt.exchange.application.port.out.SaveRoundRatePort;
import kr.co.kwt.exchange.domain.ClosingRate;
import kr.co.kwt.exchange.domain.Country;
import kr.co.kwt.exchange.domain.Round;
import kr.co.kwt.exchange.domain.RoundRate;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand.FetchRate;

@Service
@RequiredArgsConstructor
public class FetchExchangeService implements FetchExchangeUseCase {

    private final LoadRoundPort loadRoundPort;
    private final SaveRoundRatePort saveRoundRatePort;
    private final SaveClosingRatePort saveClosingRatePort;
    private final LoadRoundRatePort loadRoundRatePort;

    @Override
    @Transactional
    public FetchExchangeResult fetchExchange(@NonNull final FetchExchangeCommand command) {
        long fetchedRoundRateCount = 0;
        long fetchedClosingRateCount = 0;

        Map<String, FetchRate> currrencyCodeFetchRateMap =
                getCurrrencyCodeFetchRateMap(command);

        // 현재 회차 조회
        Round round = loadRoundPort
                .findLastRound()
                .orElseThrow(ServerException::new);

        if (command.getRound() == 1) { // 최초 회차시 종가 데이터 추가
            fetchedClosingRateCount = ClosingRateBulkInsert();
        }

        round.updateRound(command.getRound());
        fetchedRoundRateCount = RoundRateBulkInsert(command, currrencyCodeFetchRateMap);
        return new FetchExchangeResult(fetchedRoundRateCount, fetchedClosingRateCount);
    }

    private long RoundRateBulkInsert(
            @NonNull final FetchExchangeCommand command,
            @NonNull final Map<String, FetchRate> currrencyCodeFetchRateMap
    ) {
        List<RoundRate> roundRates = currrencyCodeFetchRateMap
                .values()
                .stream()
                .map(fetchRate -> fetchRate.toRoundRate(command.getRound(), command.getFetchedAt()))
                .toList();

        return saveRoundRatePort.bulkInsert(roundRates);
    }

    private long ClosingRateBulkInsert() {
        List<ClosingRate> closingRateList = loadRoundRatePort
                .findLastRoundRates()
                .stream()
                .map(roundRate -> ClosingRate.withoutId(
                        roundRate.getCurrencyCode(),
                        roundRate.getRoundRate(),
                        roundRate.getFetchedAt()))
                .toList();

        return saveClosingRatePort.bulkInsert(closingRateList);
    }

    private Map<String, FetchRate> getCurrrencyCodeFetchRateMap(
            @NonNull final FetchExchangeCommand fetchExchangeCommand
    ) {
        return fetchExchangeCommand
                .getFetchRates()
                .stream()
                .filter(fetchRate -> Country.isValidCurrencyCode(fetchRate.getCurrencyCode()))
                .collect(Collectors.toMap(FetchRate::getCurrencyCode, Function.identity()));
    }
}
