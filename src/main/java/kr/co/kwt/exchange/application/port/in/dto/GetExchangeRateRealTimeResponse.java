package kr.co.kwt.exchange.application.port.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetExchangeRateRealTimeResponse {

    @JsonProperty("rv")
    private double rateValue;
    @JsonProperty("at")
    private LocalDateTime fetchedAt;
    @JsonProperty("r")
    private int round;
}
