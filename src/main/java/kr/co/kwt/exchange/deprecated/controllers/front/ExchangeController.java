package kr.co.kwt.exchange.deprecated.controllers.front;

import kr.co.kwt.exchange.deprecated.models.Country;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

//@Controller
//@RequestMapping("/")
public class ExchangeController {

    private final List<Country> countries = Arrays.asList(
            new Country("KR", "South Korea", "KRW"),
            new Country("US", "United States", "USD"),
            new Country("JP", "Japan", "JPY"),
            new Country("CN", "China", "CNY")
    );

    @GetMapping
    public String renderExchangePage(
            Model model
    ) {
        model.addAttribute("countries", countries);
        model.addAttribute("currencies", new String[]{"KRW", "USD", "JPY", "EUR", "CNY"});
        return "exchangeRate";
    }
}