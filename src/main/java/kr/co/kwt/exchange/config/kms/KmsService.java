package kr.co.kwt.exchange.config.kms;

import kr.co.kwt.exchange.config.properties.KmsProperties;
import kr.co.kwt.exchange.config.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KmsService {

    private final KmsProperties kmsProperties;
    private final WebClientService webClientService;

    public GetKmsResponse<KmsDbSecretValue> getKmsDBSecrets() {
        return webClientService
                .getWebClient(kmsProperties.getUrl(), new KmsWebClientCustomizer(kmsProperties))
                .get()
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<GetKmsResponse<KmsDbSecretValue>>() {
                })
                .blockFirst();
    }

    public KmsDbSecretValue getDbSecretValue() {
        return getKmsDBSecrets()
                .getSecrets()
                .stream()
                .filter(secret -> secret.getSecretKey().equals(kmsProperties.getSecretKey()))
                .map(KmsSecret::getSecretValue)
                .findFirst()
                .orElseThrow(() -> new IllegalAccessError("KMS 정보 조회 실패, KMS 정보를 확인해주세요"));
    }
}
