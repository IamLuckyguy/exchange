package kr.co.kwt.exchange.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import kr.co.kwt.exchange.config.properties.WebClientProperties;
import kr.co.kwt.exchange.config.webclient.WebClientLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final WebClientProperties properties;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(WebClientLoggingFilter.logRequest())
                .filter(WebClientLoggingFilter.logResponse())
                .clientConnector(new ReactorClientHttpConnector(createHttpClient()));
    }

    private HttpClient createHttpClient() {
        ConnectionProvider provider = ConnectionProvider.builder("custom-provider")
                .maxConnections(500)
                .maxIdleTime(Duration.ofSeconds(20))
                .maxLifeTime(Duration.ofSeconds(60))
                .build();

        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeout())
                .responseTimeout(Duration.ofMillis(properties.getReadTimeout()))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(properties.getReadTimeout(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(properties.getWriteTimeout(), TimeUnit.MILLISECONDS)))
                .resolver(DefaultAddressResolverGroup.INSTANCE);
    }
}