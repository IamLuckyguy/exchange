package kr.co.kwt.exchange.domain;

import kr.co.kwt.exchange.application.InvalidCurrencyCodeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.Arrays;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public enum Country {

    KOREA("Korea", "KRW", "🇰🇷"),
    CHINA("China", "CNY", "🇨🇳"),
    JAPAN("Japan", "JPY", "🇯🇵"),
    USA("United States", "USD", "🇺🇸"),
    EURO("Eurozone", "EUR", "🇪🇺"),
    ;

    private final String countryName;
    private final String currencyCode;
    private final String countryFlag;

    public static Country of(@NonNull final String currencyCode) {
        return Arrays
                .stream(Country.values())
                .filter(country -> country.getCurrencyCode().equals(currencyCode))
                .findAny()
                .orElseThrow(InvalidCurrencyCodeException::new);
    }

    public static boolean isValidCurrencyCode(@NonNull final String currencyCode) {
        try {
            of(currencyCode);
            return true;
        } catch (InvalidCurrencyCodeException e) {
            return false;
        }
    }
}
