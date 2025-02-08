package kr.co.kwt.exchange.adapter.in.web;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.kwt.commonui.ServiceProperties;
import kr.co.kwt.exchange.utils.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExchangeViewController {

    private final WebUtils webUtils;
    private final ServiceProperties serviceProperties;

    @GetMapping("/")
    public String exchangeRate(
            Model model,
            HttpServletRequest request
    ) {
        model.addAttribute("baseUrl", webUtils.getBaseUrl(request));
        model.addAttribute("activeProfile", webUtils.getActiveProfile());

        // 서비스 목록 하드코딩
        List<ServiceProperties> services = List.of(
                new ServiceProperties("exchange", "환율계산기", "https://exchange.kwt.co.kr", "#00ff88"),
                new ServiceProperties("salary", "연봉실수령좀", "https://givemesalary.kwt.co.kr", "#ff6b6b")
        );

        // 현재 서비스 정보와 전체 서비스 목록 전달
        model.addAttribute("currentService", serviceProperties);
        model.addAttribute("services", services);

        return "pages/exchangeRate";
    }
}
