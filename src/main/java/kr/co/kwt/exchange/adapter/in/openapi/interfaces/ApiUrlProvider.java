package kr.co.kwt.exchange.adapter.in.openapi.interfaces;

import java.time.LocalDate;

public interface ApiUrlProvider {

    String getUrl(LocalDate fetchDate);

    String getPath(LocalDate fetchDate);

    String getBaseUrl();
}
