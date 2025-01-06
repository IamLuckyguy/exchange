package kr.co.kwt.exchange.openapi.interfaces;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;

import java.time.LocalDateTime;

public interface OpenApiGetExchangeResult {

    double getRate();

    String getName();

    String getCurrencyCode();

    String getLiveStatus();

    String getMarketStatus();

    String getTrend();

    double getTrendRate();

    double getTrendDiff();

    FetchExchangeCommand.FetchRate toFetchRate();

    LocalDateTime getFetchedAt();
}
