package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeRateResponse {

    private String countryName;
    private String countryFlag;
    private String currencyCode;

    @Setter
    private List<GetExchangeRateHistoryResponse> exchangeRateHistories;

    @Setter
    private List<GetExchangeRateRealTimeResponse> exchangeRateRealTimeResponses;
    private Integer unitAmount;
    private Integer decimals;
    private LocalDateTime fetchedAt;
    private LocalDateTime updatedAt;

}
