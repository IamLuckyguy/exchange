package kr.co.kwt.exchange.config.kms;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class KmsDbSecretValue {

    private String driver;
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
}