package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Deprecated
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rate {

    private double value;
    private LocalDateTime updatedAt;
}
