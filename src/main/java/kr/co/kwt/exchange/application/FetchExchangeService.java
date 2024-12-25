package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;
import kr.co.kwt.exchange.application.port.in.FetchExchangeUseCase;
import kr.co.kwt.exchange.application.port.out.LoadExchangePort;
import kr.co.kwt.exchange.application.port.out.LoadRoundPort;
import kr.co.kwt.exchange.application.port.out.SaveExchangePort;
import kr.co.kwt.exchange.domain.Exchange;
import kr.co.kwt.exchange.domain.Round;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand.FetchRate;

@Service
@RequiredArgsConstructor
public class FetchExchangeService implements FetchExchangeUseCase {

    private final LoadRoundPort loadRoundPort;
    private final LoadExchangePort loadExchangePort;
    private final SaveExchangePort saveExchangePort;

    @Override
    @Transactional
    public void fetchExchange(@NonNull final FetchExchangeCommand fetchExchangeCommand) {
        Map<String, FetchRate> currrencyCodeFetchRateMap =
                getCurrrencyCodeFetchRateMap(fetchExchangeCommand);

        // 현재 회차 조회
        Round round = loadRoundPort
                .findLastRound()
                .orElseThrow(ServerException::new);

        // 저장되어 있는 회차랑 값이 다를 때 (local < remote : 다음 회차, remote = 1 : 다음날의 신규 회차)
        if (round.getRound().compareTo(fetchExchangeCommand.getRound()) != 0) {
            // fetch
            List<Exchange> exchanges = loadExchangePort.findAll();
            exchanges.forEach(exchange -> doFetch(
                    fetchExchangeCommand,
                    exchange,
                    currrencyCodeFetchRateMap));

            saveExchangePort.saveAll(exchanges);
            // round 업데이트
            round.updateRound(fetchExchangeCommand.getRound());
        }
    }

    private void doFetch(
            @NonNull final FetchExchangeCommand fetchExchangeCommand,
            @NonNull final Exchange exchange,
            @NonNull final Map<String, FetchRate> currrencyCodeFetchRateMap
    ) {
        getFetchRateFrom(currrencyCodeFetchRateMap, exchange.getCurrencyCode())
                .ifPresent(fetchRate -> exchange.fetch(
                        fetchRate.getRoundRate(),
                        fetchExchangeCommand.getRound(),
                        fetchRate.getTrend(),
                        fetchRate.getTrendRate(),
                        fetchRate.getLiveStatus(),
                        fetchRate.getMarketStatus(),
                        fetchExchangeCommand.getFetchedAt()));
    }

    private Map<String, FetchRate> getCurrrencyCodeFetchRateMap(
            @NonNull final FetchExchangeCommand fetchExchangeCommand
    ) {
        return fetchExchangeCommand
                .getFetchRates()
                .stream()
                .collect(Collectors.toMap(FetchRate::getCurrencyCode, Function.identity()));
    }

    private Optional<FetchRate> getFetchRateFrom(
            @NonNull final Map<String, FetchRate> currencyCodeFetchRateMap,
            @NonNull final String currencyCode
    ) {
        return Optional.ofNullable(currencyCodeFetchRateMap.getOrDefault(currencyCode, null));
    }
}
