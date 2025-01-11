package kr.co.kwt.exchange.config.kms;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class KmsRedisSecretValue {

    private String host;
    private Integer port;
    private String master;
    private List<String> nodes;
    private String password;
}
