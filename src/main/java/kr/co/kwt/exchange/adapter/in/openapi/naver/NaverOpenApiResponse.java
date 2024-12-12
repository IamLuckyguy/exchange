package kr.co.kwt.exchange.adapter.in.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NaverOpenApiResponse implements OpenApiResponse {

    @JsonProperty("closePrice")
    private String rateValue;

    @JsonProperty("exchangeCode")
    private String currencyCode;

    public Double getRateValue() {
        return Double.parseDouble(rateValue.replaceAll(",", ""));
    }
}
