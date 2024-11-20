package kr.co.kwt.exchange.controllers.front;

import kr.co.kwt.exchange.services.ExchangeService;
import kr.co.kwt.exchange.utils.DateTimeUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ExchangeController {

    @GetMapping
    public String renderExchangePage(
            Model model
    ) {
        model.addAttribute("currencies", new String[]{"KRW", "USD", "JPY", "EUR", "CNY"});
        return "exchange";
    }
}