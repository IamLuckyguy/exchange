package kr.co.kwt.exchange.config.kms;

import kr.co.kwt.exchange.config.webclient.WebClientCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
public class KmsWebClientCustomizer implements WebClientCustomizer {

    private final String KMS_HEADER = "x-api-key";
    private final String apiKey;

    @Override
    public WebClient customize(final WebClient webClient) {
        return webClient
                .mutate()
                .defaultHeader(KMS_HEADER, apiKey)
                .build();
    }
}
