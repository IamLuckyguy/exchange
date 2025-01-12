package kr.co.kwt.exchange.adapter.in.web.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SseSendRequest<T> {

    T body;
}
