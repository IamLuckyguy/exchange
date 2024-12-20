package kr.co.kwt.exchange.adapter.in.openapi.naver;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.ApiUrlProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(name = "openapi.naver.enable", havingValue = "true")
public class NaverApiUrlProvider implements ApiUrlProvider {

    @Value("${openapi.naver.base-url}")
    private String baseUrl;
    @Value("${openapi.naver.path.exchange-rate}")
    private String exchangeRatePath;
    @Value("${openapi.naver.path.degree-count}")
    private String degreeCountPath;


    @Override
    public String getApiUrl() {
        return baseUrl + exchangeRatePath;
    }

    @Override
    public String getApiUrl(LocalDateTime localDateTime) {
        return getApiUrl();
    }

    @Override
    public String getDegreeApiUrl() {
        return baseUrl + degreeCountPath;
    }

    @Override
    public String getApiUrl(LocalDate fetchDate) {
        return getApiUrl();
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
