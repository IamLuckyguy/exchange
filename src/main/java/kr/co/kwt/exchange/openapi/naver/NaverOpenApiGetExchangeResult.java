package kr.co.kwt.exchange.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.application.port.dto.FetchExchangeCommand;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiGetExchangeResult;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class NaverOpenApiGetExchangeResult implements OpenApiGetExchangeResult {

    private String name;

    @JsonProperty("exchangeCode")
    private String currencyCode;

    @JsonProperty("closePrice")
    private String rate;

    @JsonProperty("fluctuationsType")
    private Map<String, String> trend;

    @JsonProperty("fluctuations")
    private String trendDiff;

    @JsonProperty("fluctuationsRatio")
    private String trendRate;

    @JsonProperty("delayTimename")
    private String liveStatus;

    @JsonProperty("marketStatus")
    private String marketStatus;

    @JsonProperty("localTradedAt")
    private String fetchedAt;

    @Override
    public double getRate() {
        return Double.parseDouble(rate.replace(",", ""));
    }

    @Override
    public double getTrendRate() {
        return Double.parseDouble(trendRate.replace(",", ""));
    }

    @Override
    public double getTrendDiff() {
        return Double.parseDouble(trendDiff.replace(",", ""));
    }

    @Override
    public String getTrend() {
        return trend.get("text");
    }

    @Override
    public FetchExchangeCommand.FetchRate toFetchRate() {
        return new FetchExchangeCommand.FetchRate(
                getCurrencyCode(),
                getRate(),
                getTrend(),
                getTrendRate(),
                getTrendDiff(),
                getMarketStatus(),
                getLiveStatus(),
                getFetchedAt());
    }

    @Override
    public LocalDateTime getFetchedAt() {
        OffsetDateTime parse = OffsetDateTime.parse(fetchedAt);
        return parse.toLocalDateTime();
    }
}
