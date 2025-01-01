package kr.co.kwt.exchange.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class WebUtils {
    private final Environment environment;

    public WebUtils(Environment environment) {
        this.environment = environment;
    }

//    public String getBaseUrl(ServerHttpRequest request) {
//        URI uri = request.getURI();
//        return String.format("%s://%s%s%s",
//                uri.getScheme(),
//                uri.getHost(),
//                getPortString(uri.getPort()),
//                uri.getPath()
//        );
//    }

    public String getBaseUrl(HttpServletRequest request) {
        return String.format("%s://%s%s%s",
                request.getScheme(),
                request.getRemoteHost(),
                getPortString(request.getServerPort()),
                request.getRequestURI()
        );
    }

    public String getActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        return activeProfiles.length > 0 ? activeProfiles[0] : "local";
    }

    private String getPortString(int port) {
        return (port != 80 && port != 443 && port != -1) ? ":" + port : "";
    }
}