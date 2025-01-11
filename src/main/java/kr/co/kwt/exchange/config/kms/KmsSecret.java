package kr.co.kwt.exchange.config.kms;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KmsSecret {
    private String id;
    private String secretType;
    private String secretKey;
    private Object secretValue;
    private String description;
    private LocalDateTime lastModified;
}