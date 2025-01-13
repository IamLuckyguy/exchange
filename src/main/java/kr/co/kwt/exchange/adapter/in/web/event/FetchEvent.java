package kr.co.kwt.exchange.adapter.in.web.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FetchEvent {

    private String event;
    private FetchEventResponse message;
}
