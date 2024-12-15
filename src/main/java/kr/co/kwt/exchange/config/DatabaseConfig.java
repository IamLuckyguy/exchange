package kr.co.kwt.exchange.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import kr.co.kwt.exchange.config.kms.KmsDbSecretValue;
import kr.co.kwt.exchange.config.kms.KmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableR2dbcAuditing
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "kr.co.kwt.exchange.adapter.out.persistence")
public class DatabaseConfig {

    @Bean
    public ConnectionFactory connectionFactory(final KmsService kmsService) {
        KmsDbSecretValue kmsDbSecretValue = kmsService.getDbSecretValue();
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, "mysql")
                .option(ConnectionFactoryOptions.HOST, kmsDbSecretValue.getHost())
                .option(ConnectionFactoryOptions.PORT, kmsDbSecretValue.getPort())
                .option(ConnectionFactoryOptions.DATABASE, kmsDbSecretValue.getDatabase())
                .option(ConnectionFactoryOptions.USER, kmsDbSecretValue.getUsername())
                .option(ConnectionFactoryOptions.PASSWORD, kmsDbSecretValue.getPassword())
                .build());
    }

    @Bean
    public ConnectionFactoryInitializer initializer(final ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        return initializer;
    }

    @Bean
    public ReactiveTransactionManager transactionManager(final ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}