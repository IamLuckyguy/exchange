package kr.co.kwt.exchange.config.kms;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetKmsResponse {

    private String serviceId;
    private String environment;
    private List<KmsSecret> secrets;

    @Getter
    public static class KmsSecret {
        private String id;
        private String secretType;
        private String secretKey;
        private KmsDBSecretValue secretValue;
        private String description;
        private LocalDateTime lastModified;
    }

    @ToString
    @Getter
    public static class KmsDBSecretValue {
        private String host;
        private Integer port;
        private String database;
        private String username;
        private String password;
    }
}
