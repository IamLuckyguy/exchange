package kr.co.kwt.exchange.config.kms;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KmsSecret<T> {
    private String id;
    private String secretType;
    private String secretKey;
    private T secretValue;
    private String description;
    private LocalDateTime lastModified;
}