package kr.co.kwt.exchange.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.openapi.interfaces.OpenApiGetRoundResult;
import lombok.Data;

@Data
public class NaverOpenApiGetRoundResult implements OpenApiGetRoundResult {

    @JsonProperty("degreeCount")
    private int round;
}
