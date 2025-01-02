package kr.co.kwt.exchange.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverOpenApiGetRoundBody {

    @JsonProperty("result")
    private NaverOpenApiGetRoundResult result;
}
