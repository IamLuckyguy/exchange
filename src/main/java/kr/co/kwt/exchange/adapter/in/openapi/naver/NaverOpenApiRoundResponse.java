package kr.co.kwt.exchange.adapter.in.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiRoundResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverOpenApiRoundResponse implements OpenApiRoundResponse {

    @JsonProperty("degreeCount")
    private int round;
}
