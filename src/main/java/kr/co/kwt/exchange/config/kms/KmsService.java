package kr.co.kwt.exchange.config.kms;

import kr.co.kwt.exchange.config.properties.KmsProperties;
import kr.co.kwt.exchange.config.webclient.WebClientCustomizer;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KmsService {

    public static final String KMS_HEADER = "x-api-key";
    private final KmsProperties kmsProperties;
    private final WebClientService webClientService;

    public GetKmsResponse getKms() {
        return webClientService
                .getWebClient(kmsProperties.getUrl(), getWebClientCustomizer())
                .get()
                .retrieve()
                .bodyToFlux(GetKmsResponse.class)
                .blockFirst();
    }

    private WebClientCustomizer getWebClientCustomizer() {
        return webClient -> webClient
                .mutate()
                .defaultHeader(KMS_HEADER, kmsProperties.getApiKey())
                .build();
    }

    public GetKmsResponse.KmsDBSecretValue getDbSecretValue() {
        return getKms()
                .getSecrets()
                .stream()
                .filter(secret -> secret.getSecretKey().equals(kmsProperties.getSecretKey()))
                .map(GetKmsResponse.KmsSecret::getSecretValue)
                .findFirst()
                .orElseThrow(() -> new IllegalAccessError("KMS 정보 조회 실패, KMS 정보를 확인해주세요"));
    }
}
