package kr.co.kwt.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeApplication.class, args);
    }

}
