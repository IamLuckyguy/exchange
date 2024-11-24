package kr.co.kwt.exchange.deprecated.services;

import kr.co.kwt.exchange.adpater.out.persistence.repositories.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

//@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final WebClient.Builder webClientBuilder;
    private final ExchangeRateRepository repository;

//    @Value("${exchange.api.key}")
//    private String apiKey;
//
//    @Value("${exchange.api.base-url}")
//    private String baseUrl;
//
//    public Flux<ExchangeRates> fetchAndSaveExchangeRates(String searchDate) {
//        WebClient webClient = WebClientUtils.getOrCreateWebClient(webClientBuilder, baseUrl);
//
//        return webClient.get()
//                .uri("/site/program/financial/exchangeJSON?authkey={apiKey}&searchdate={searchDate}&data=AP01",
//                        apiKey, searchDate)
//                .retrieve()
//                .bodyToFlux(ExchangeRateResponse.class)
//                .filter(response -> response.getResult() == 1)
//                .map(response -> ExchangeRates.builder()
//                        .result(response.getResult())
//                        .curUnit(response.getCurUnit())
//                        .curNm(response.getCurNm())
//                        .ttb(response.getTtb())
//                        .tts(response.getTts())
//                        .dealBasR(response.getDealBasR())
//                        .bkpr(response.getBkpr())
//                        .yyEfeeR(response.getYyEfeeR())
//                        .tenDdEfeeR(response.getTenDdEfeeR())
//                        .kftcDealBasR(response.getKftcDealBasR())
//                        .kftcBkpr(response.getKftcBkpr())
//                        .searchDate(searchDate)
//                        .build())
//                .flatMap(repository::save);
//    }
//
//    public Flux<ExchangeRates> getExchangeRates(String searchDate) {
//        return repository.findBySearchDate(searchDate);
//    }
//
//    public Flux<ExchangeRates> getTodayExchangeRates() {
//        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
//        return getExchangeRates(today);
//    }
}