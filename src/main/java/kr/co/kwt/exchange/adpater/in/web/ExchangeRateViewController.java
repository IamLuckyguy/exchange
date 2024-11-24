package kr.co.kwt.exchange.adpater.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ExchangeRateViewController {

    @GetMapping("/")
    public String exchangeRate() {
        return "exchangeRate";
    }
}
