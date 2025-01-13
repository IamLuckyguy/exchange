package kr.co.kwt.exchange.config.kms;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KmsSecretType {
    // 데이터 저장소
    DB("db"),
    REDIS("redis"),
    MONGODB("mongodb"),
    ELASTICSEARCH("elasticsearch"),
    CASSANDRA("cassandra"),

    // 메시징/스트리밍
    KAFKA("kafka"),
    RABBITMQ("rabbitmq"),
    AWS_SQS("aws-sqs"),
    AWS_SNS("aws-sns"),

    // 인증/인가
    OAUTH("oauth"),
    JWT("jwt"),
    SAML("saml"),
    LDAP("ldap"),

    // 보안/인증서
    CERT("cert"),
    SSL("ssl"),
    SSH("ssh"),
    GPG("gpg"),

    // API/엔드포인트
    API("api"),
    GRAPHQL("graphql"),
    GRPC("grpc"),
    WEBHOOK("webhook"),

    // 클라우드 서비스
    AWS("aws"),
    GCP("gcp"),
    AZURE("azure"),

    // 모니터링/로깅
    PROMETHEUS("prometheus"),
    GRAFANA("grafana"),
    ELK("elk"),
    DATADOG("datadog"),

    // 기타 인프라
    CDN("cdn"),
    DNS("dns"),
    SMTP("smtp"),
    FTP("ftp"),

    // 빈 값
    EMPTY("");

    private final String type;

    public static KmsSecretType fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return EMPTY;
        }

        for (KmsSecretType type : values()) {
            if (type.type.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown KmsSecretType value: " + value);
    }
}
