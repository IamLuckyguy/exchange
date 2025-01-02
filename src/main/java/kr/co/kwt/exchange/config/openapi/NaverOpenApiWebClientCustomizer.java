package kr.co.kwt.exchange.config.openapi;

import kr.co.kwt.exchange.config.webclient.WebClientCustomizer;
import org.springframework.web.reactive.function.client.WebClient;

public class NaverOpenApiWebClientCustomizer implements WebClientCustomizer {

    @Override
    public WebClient customize(final WebClient webClient) {
        return webClient.mutate()
                .defaultHeaders(headers -> {
                    headers.add("Accept", "application/json, text/plain, */*");
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
