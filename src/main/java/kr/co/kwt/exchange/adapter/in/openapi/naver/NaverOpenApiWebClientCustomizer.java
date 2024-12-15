package kr.co.kwt.exchange.adapter.in.openapi.naver;

import kr.co.kwt.exchange.config.webclient.WebClientCustomizer;
import kr.co.kwt.exchange.config.webclient.WebClientLoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

public class NaverOpenApiWebClientCustomizer implements WebClientCustomizer {

    @Override
    public WebClient customize(final WebClient webClient) {

        return webClient.mutate()
                .filter(WebClientLoggingFilter.logRequest())
                .filter(WebClientLoggingFilter.logResponse())
                .defaultHeaders(headers -> {
                    headers.add("Host", "m.stock.naver.com");
                    headers.add("Accept", "application/json, text/plain, */*");
                    headers.add("Referer", "https://m.stock.naver.com/");
//                    headers.add("Accept-Encoding", "gzip, deflate, br, zstd");
                    headers.add("Accept-Language", "ko,ko-KR;q=0.9,en;q=0.8");
                    headers.add("Sec-Ch-Ua", "\"Google Chrome\";v=\"131\", \"Chromium\";v=\"131\", \"Not_A Brand\";v=\"24\"");
                    headers.add("Sec-Ch-Ua-Mobile", "?0");
                    headers.add("Sec-Ch-Ua-Platform", "\"macOS\"");
                    headers.add("Sec-Fetch-Dest", "empty");
                    headers.add("Sec-Fetch-Mode", "cors");
                    headers.add("Sec-Fetch-Site", "same-origin");
                    headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
                })
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