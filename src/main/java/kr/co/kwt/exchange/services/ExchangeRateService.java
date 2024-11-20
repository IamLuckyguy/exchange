package kr.co.kwt.exchange.services;

import kr.co.kwt.exchange.models.ExchangeRate;
import kr.co.kwt.exchange.models.ExchangeRateResponse;
import kr.co.kwt.exchange.repositories.ExchangeRateRepository;
import kr.co.kwt.exchange.utils.WebClientUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final WebClient.Builder webClientBuilder;
    private final ExchangeRateRepository repository;

    @Value("${exchange.api.key}")
    private String apiKey;

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    public Flux<ExchangeRate> fetchAndSaveExchangeRates(String searchDate) {
        WebClient webClient = WebClientUtils.getOrCreateWebClient(webClientBuilder, baseUrl);

        return webClient.get()
                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
                        apiKey, searchDate)
                .retrieve()
                .bodyToFlux(ExchangeRateResponse.class)
                .filter(response -> response.getResult() == 1)
                .map(response -> ExchangeRate.builder()
                        .result(response.getResult())
                        .curUnit(response.getCurUnit())
                        .curNm(response.getCurNm())
                        .ttb(response.getTtb())
                        .tts(response.getTts())
                        .dealBasR(response.getDealBasR())
                        .bkpr(response.getBkpr())
                        .yyEfeeR(response.getYyEfeeR())
                        .tenDdEfeeR(response.getTenDdEfeeR())
                        .kftcDealBasR(response.getKftcDealBasR())
                        .kftcBkpr(response.getKftcBkpr())
                        .searchDate(searchDate)
                        .build())
                .flatMap(repository::save);
    }

    public Flux<ExchangeRate> getExchangeRates(String searchDate) {
        return repository.findBySearchDate(searchDate);
    }

    public Flux<ExchangeRate> getTodayExchangeRates() {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return getExchangeRates(today);
    }
}