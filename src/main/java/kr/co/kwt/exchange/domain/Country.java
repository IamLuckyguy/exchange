package kr.co.kwt.exchange.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@Table("countries")
@Getter
@AllArgsConstructor
public final class Country {

    private String countryName;
    private String currencyCode;
    private String countryFlag;

}
