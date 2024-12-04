package kr.co.kwt.exchange.adapter.out;

import kr.co.kwt.exchange.config.webclient.WebClientCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ExchangeRateWebClientCustomizer implements WebClientCustomizer {
    @Override
    public WebClient customize(WebClient webClient) {
        return webClient.mutate()
                .defaultHeader("Accept", "application/json")
                // 필요한 커스텀 설정 추가
                .build();
    }
}

/*
 * // 다른 서비스의 WebClient 커스터마이징 예시
 * @Component
 * public class AnotherServiceWebClientCustomizer implements WebClientCustomizer {
 *     @Override
 *     public WebClient customize(WebClient webClient) {
 *         return webClient.mutate()
 *                 .defaultHeader("Custom-Header", "value")
 *                 .filter((request, next) -> {
 *                     // 커스텀 필터 로직
 *                     return next.exchange(request);
 *                 })
 *                 .build();
 *     }
 * }
 */