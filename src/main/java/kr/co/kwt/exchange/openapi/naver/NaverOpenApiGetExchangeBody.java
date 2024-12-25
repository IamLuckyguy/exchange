package kr.co.kwt.exchange.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NaverOpenApiGetExchangeBody {

    @JsonProperty("result")
    private List<NaverOpenApiGetExchangeResult> results;
}
