package kr.co.kwt.exchange.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.reactive.function.server.ServerRequest;

public class PageableUtils {

    public static Pageable getPageable(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));

        if (page < 0) {
            page = 0;
        }
        if (size < 1 || size > 50) {
            size = 10;
        }

        return PageRequest.of(page, size);
    }
}