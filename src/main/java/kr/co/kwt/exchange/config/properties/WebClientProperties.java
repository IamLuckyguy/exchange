package kr.co.kwt.exchange.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {
    private int connectTimeout = 5000;
    private int readTimeout = 5000;
    private int writeTimeout = 5000;
    private int maxMemorySize = 16777216; // 16MB
    private int maxRetries = 3;
}