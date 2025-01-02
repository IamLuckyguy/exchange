package kr.co.kwt.exchange.application.port.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetRoundResult {

    private Integer round;
    private LocalDateTime fetchedAt;
}
