package kr.co.kwt.exchange.adapter.in.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverOpenApiRoundBody {

    @JsonProperty("result")
    private NaverOpenApiRoundResponse result;

}
