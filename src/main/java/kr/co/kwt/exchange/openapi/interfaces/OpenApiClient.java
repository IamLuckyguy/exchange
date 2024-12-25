package kr.co.kwt.exchange.openapi.interfaces;

import java.util.List;

public interface OpenApiClient {

    OpenApiGetRoundResult getRound();

//    OpenApiGetExchangeResult getExchange(OpenApiRequest openApiRequest);

    List<OpenApiGetExchangeResult> getExchange(OpenApiRequest openApiRequest);
}
