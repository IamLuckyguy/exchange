package kr.co.kwt.exchange.adapter.in.openapi.koreaexim;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.ApiUrlProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class KoreaEximApiUrlProvider implements ApiUrlProvider {

    @Value("${exchange.koreaexim.api.key}")
    private String apiKey;

    @Value("${exchange.koreaexim.api.base-url}")
    private String baseUrl;


    @Override
    public String getUrl(final LocalDate fetchDate) {
        return getBaseUrl();
    }

    @Override
    public String getPath(LocalDate fetchDate) {
        return String.format("/site/program/financial/exchangeJSON?authkey=%s&searchdate=%s&data=AP01",
                apiKey, fetchDate);
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
