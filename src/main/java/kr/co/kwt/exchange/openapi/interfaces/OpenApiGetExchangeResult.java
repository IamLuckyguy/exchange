package kr.co.kwt.exchange.openapi.interfaces;

import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;

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
}
