package kr.co.kwt.exchange.adpater.in.api;

import kr.co.kwt.exchange.utils.WebClientUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateFetcher {

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;

    public Flux<FetchExchangeRateResponse> fetchAndSaveExchangeRates(String searchDate) {
        WebClient webClient = WebClientUtils.getOrCreateWebClient(webClientBuilder, baseUrl);

        return webClient.get()
                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
                        apiKey, searchDate)
                .retrieve()
                .bodyToFlux(FetchExchangeRateResponse.class)
                .filter(response -> response.getResult() == 1);
    }

//    public Flux<ExchangeRates> getExchangeRates(String searchDate) {
//        return repository.findBySearchDate(searchDate);
//    }
//
//    public Flux<ExchangeRates> getTodayExchangeRates() {
//        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
//        return getExchangeRates(today);
//    }

}
