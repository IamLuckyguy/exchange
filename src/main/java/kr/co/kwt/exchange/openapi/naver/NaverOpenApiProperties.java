package kr.co.kwt.exchange.openapi.naver;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "openapi.naver")
public class NaverOpenApiProperties {

    private String key;
    private String baseUrl;
    private Map<String, String> path;

    public String getExchangeUrl() {
        return baseUrl + "/" + path.get("exchange");
    }

    public String getRoundUrl() {
        return baseUrl + "/" + path.get("round");
    }

}
