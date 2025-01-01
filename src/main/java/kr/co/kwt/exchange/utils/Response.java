package kr.co.kwt.exchange.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

    String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    T result;

    public Response(String description) {
        this.description = description;
    }

    public Response(T result, String description) {
        this.result = result;
        this.description = description;
    }

    public static <R> Response<R> ok(R result) {
        return new Response<>(result, "API 호출에 성공했습니다.");
    }

    public static <R> Response<R> ok(R result, String description) {
        return new Response<>(result, description);
    }
}
