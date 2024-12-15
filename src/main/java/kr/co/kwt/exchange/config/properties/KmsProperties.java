package kr.co.kwt.exchange.config.properties;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@ToString
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "kms")
public class KmsProperties {

    @NotEmpty
    private String url;
    @NotEmpty
    private String apiKey;
    @NotEmpty
    private String secretKey;
}
