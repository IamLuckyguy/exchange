package kr.co.kwt.exchange.adapter.in.openapi.interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ApiUrlProvider {

    String getApiUrl();

    String getApiUrl(LocalDateTime localDateTime);

    String getDegreeApiUrl();

    String getApiUrl(LocalDate fetchDate);

    String getBaseUrl();
}
