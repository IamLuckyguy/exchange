package kr.co.kwt.exchange.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.kwt.exchange.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ExchangeViewController {

    private final WebUtils webUtils;

    @GetMapping("/")
    public String exchangeRate(
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("baseUrl", webUtils.getBaseUrl(request));
        model.addAttribute("activeProfile", webUtils.getActiveProfile());
        return "pages/exchangeRate";
    }
}
