package kr.co.kwt.exchange.utils;

import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebClientUtils {
    private static final Map<String, WebClient> webClientCache = new ConcurrentHashMap<>();

    public static WebClient getOrCreateWebClient(WebClient.Builder webClientBuilder, String baseUrl) {
        return webClientCache.computeIfAbsent(baseUrl,
                url -> webClientBuilder
                        .baseUrl(url)
                        .build());
    }

    public static void clearWebClientCache() {
        webClientCache.clear();
    }
}