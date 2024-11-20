package kr.co.kwt.exchange.utils;

import com.kidari.lectureapi.common.constants.ValidationType;
import com.kidari.lectureapi.common.exceptions.InvalidInputException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class ValidationUtil {
    public static Mono<ServerResponse> validateRequest(ValidationType type, String name, ServerRequest request) {
        return Mono.just(request).doOnNext(req -> {
            String value = switch (type) {
                case PATH_VARIABLE -> req.pathVariable(name);
                case QUERY_PARAM -> req.queryParam(name).orElse(null);
                default -> null;
            };

            if (value == null || value.trim().isEmpty()) {
                throw new InvalidInputException(name + "는 필수입니다.");
            }
        }).then(Mono.empty());
    }
}