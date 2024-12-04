package kr.co.kwt.exchange.config.webclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class WebClientLoggingFilter {
    private static final AtomicLong REQUEST_ID = new AtomicLong(0);

    public static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            long requestId = REQUEST_ID.incrementAndGet();
            logRequestDetails(requestId, request);
            return Mono.just(ClientRequest.from(request)
                    .header("X-Request-ID", String.valueOf(requestId))
                    .build());
        });
    }

    public static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            String requestId = response.headers().header("X-Request-ID").stream()
                    .findFirst()
                    .orElse("N/A");
            logResponseDetails(requestId, response);
            return Mono.just(response);
        });
    }

    private static void logRequestDetails(long requestId, ClientRequest request) {
        if (isGetMethod(request)) {
            log.info("[{}] Request: {} {} with query params: {}",
                    requestId,
                    request.method(),
                    request.url(),
                    request.url().getQuery());
        } else {
            log.info("[{}] Request: {} {}",
                    requestId,
                    request.method(),
                    request.url());
        }
    }

    private static void logResponseDetails(String requestId, ClientResponse response) {
        log.info("[{}] Response Status: {}", requestId, response.statusCode());
    }

    private static boolean isGetMethod(ClientRequest request) {
        return HttpMethod.GET.equals(request.method());
    }
}