package kr.co.kwt.exchange.config.kms;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
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
    private final ObjectMapper objectMapper;
    private GetKmsResponse getKmsResponse;

    @PostConstruct
    public void init() {
        getKmsResponse = webClientService
                .getWebClient(kmsProperties.getUrl(), new KmsWebClientCustomizer(kmsProperties.getApiKey()))
                .get()
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<GetKmsResponse>() {
                })
                .blockFirst();
    }

    public KmsDbSecretValue getDbSecretValue() {
        return objectMapper.convertValue(getKmsResponse
                        .getSecrets()
                        .stream()
                        .filter(secret -> secret.getSecretKey().equals(kmsProperties.getSecretKey()))
                        .filter(secret -> secret.getSecretType().equals(KmsSecretType.DB.getType()))
                        .map(KmsSecret::getSecretValue)
                        .findFirst()
                        .orElseThrow(() -> new IllegalAccessError("KMS DB 정보 조회 실패, KMS 정보를 확인해주세요")),
                KmsDbSecretValue.class);
    }

    public KmsRedisSecretValue getRedisSecretValue() {
        return objectMapper.convertValue(getKmsResponse
                        .getSecrets()
                        .stream()
                        .filter(secret -> secret.getSecretKey().equals(kmsProperties.getSecretKey()))
                        .filter(secret -> secret.getSecretType().equals(KmsSecretType.REDIS.getType()))
                        .map(KmsSecret::getSecretValue)
                        .findFirst()
                        .orElseThrow(() -> new IllegalAccessError("KMS REDIS 정보 조회 실패, KMS 정보를 확인해주세요")),
                KmsRedisSecretValue.class
        );
    }
}
