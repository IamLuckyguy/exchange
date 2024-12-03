package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    private Long id;
    private String currencyCode;
    private String countryName;
    private String countryFlag;
}
