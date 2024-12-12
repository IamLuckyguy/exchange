package kr.co.kwt.exchange.adapter.in.openapi.naver;

import kr.co.kwt.exchange.adapter.in.openapi.interfaces.OpenApiRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NaverOpenApiRequest implements OpenApiRequest {

    private LocalDate fetchDate;
}
