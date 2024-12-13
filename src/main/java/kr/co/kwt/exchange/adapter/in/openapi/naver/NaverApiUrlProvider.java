package kr.co.kwt.exchange.adapter.in.openapi.naver;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.ApiUrlProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@ConditionalOnProperty(name = "openapi.naver.enable", havingValue = "true")
public class NaverApiUrlProvider implements ApiUrlProvider {

    @Value("${openapi.naver.base-url}")
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
