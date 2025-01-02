package kr.co.kwt.exchange.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import kr.co.kwt.exchange.config.kms.KmsDbSecretValue;
import kr.co.kwt.exchange.config.kms.KmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(KmsService kmsService) {
        KmsDbSecretValue dbSecretValue = kmsService.getDbSecretValue();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl(dbSecretValue.getJdbcUrl() + "?rewriteBatchedStatements=true&maxQuerySizeToLog=999999");
        hikariConfig.setUsername(dbSecretValue.getUsername());
        hikariConfig.setPassword(dbSecretValue.getPassword());

        // 풀 크기 설정
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setIdleTimeout(1800000);
        hikariConfig.setConnectionTimeout(3000);
        hikariConfig.setMaxLifetime(7200000);

        // 커넥션 유효성 검사
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.setValidationTimeout(2000);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}