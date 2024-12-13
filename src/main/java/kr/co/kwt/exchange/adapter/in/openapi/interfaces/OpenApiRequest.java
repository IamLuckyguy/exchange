package kr.co.kwt.exchange.adapter.in.openapi.interfaces;

import java.time.LocalDateTime;

public interface OpenApiRequest {

    void setFetchDate(LocalDateTime fetchDate);

    LocalDateTime getFetchDate();
}
