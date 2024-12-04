package kr.co.kwt.exchange.config.webclient;

import org.springframework.web.reactive.function.client.WebClient;

public interface WebClientCustomizer {
    WebClient customize(WebClient webClient);
}