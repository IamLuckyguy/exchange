package kr.co.kwt.exchange.config.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class WebClientService {
    private final Map<String, WebClient> webClientCache = new ConcurrentHashMap<>();
    private final WebClient.Builder webClientBuilder;

    public WebClient getWebClient(String baseUrl) {
        return webClientCache.computeIfAbsent(baseUrl, this::createWebClient);
    }

    public WebClient getWebClient(String baseUrl, WebClientCustomizer customizer) {
        String cacheKey = baseUrl + customizer.getClass().getName();
        return webClientCache.computeIfAbsent(cacheKey,
                key -> customizer.customize(createWebClient(baseUrl)));
    }

    private WebClient createWebClient(String baseUrl) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public void clearCache() {
        webClientCache.clear();
    }

    public void removeWebClient(String baseUrl) {
        webClientCache.remove(baseUrl);
    }
}