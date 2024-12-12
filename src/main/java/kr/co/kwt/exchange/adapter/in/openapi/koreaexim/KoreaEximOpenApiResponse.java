package kr.co.kwt.exchange.adapter.in.openapi.koreaexim;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class KoreaEximOpenApiResponse implements OpenApiResponse {

    @JsonProperty("cur_unit")
    private String currencyCode;

    @JsonProperty("ttb")
    private String rateValue;

    public Double getRateValue() {
        return Double.parseDouble(rateValue.replaceAll(",", ""));
    }

}
