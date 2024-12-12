package kr.co.kwt.exchange.adapter.in.openapi.naver;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.ApiUrlProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class NaverApiUrlProvider implements ApiUrlProvider {

    @Value("${exchange.naver.api.key}")
    private String apiKey;

    @Value("${exchange.naver.api.base-url}")
    private String baseUrl;


    @Override
    public String getUrl(final LocalDate fetchDate) {
        return getBaseUrl();
    }

    @Override
    public String getPath(LocalDate fetchDate) {
        return "";
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
