package kr.co.kwt.exchange.application.port.in.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class UpdateRateResponse {

    private double rateValue;
    private LocalDateTime updateTime;

    public static UpdateRateResponse of(final double rateValue, final LocalDateTime updateTime) {
        return new UpdateRateResponse(rateValue, updateTime);
    }

}
