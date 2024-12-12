package kr.co.kwt.exchange.adapter.in.openapi.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverOpenApiBody {

    @JsonProperty("result")
    private List<NaverOpenApiResponse> results;
}
