package kr.co.kwt.exchange.config.kms;

import kr.co.kwt.exchange.config.properties.KmsProperties;
import kr.co.kwt.exchange.config.webclient.WebClientCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class KmsWebClientCustomizer implements WebClientCustomizer {

    public static final String KMS_HEADER = "x-api-key";
    private final KmsProperties kmsProperties;

    @Override
    public WebClient customize(final WebClient webClient) {
        return webClient
                .mutate()
                .defaultHeader(KMS_HEADER, kmsProperties.getApiKey())
                .build();
    }
}
