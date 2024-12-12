package kr.co.kwt.exchange.adapter.in.openapi.interfaces;

import java.time.LocalDate;

public interface OpenApiRequest {

    void setFetchDate(LocalDate fetchDate);

    LocalDate getFetchDate();
}
