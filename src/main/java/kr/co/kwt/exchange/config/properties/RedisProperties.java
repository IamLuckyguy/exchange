package kr.co.kwt.exchange.config.properties;

//@Data
//@Validated
//@Component
//@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

    private String host;
    private Integer port;
    private String username;
    private String password;

}
