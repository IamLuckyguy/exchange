package kr.co.kwt.exchange.config.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다"),

    // Client Error
    INVALID_INPUT_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 입력값입니다"),
    INVALID_TYPE_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 타입이 입력되었습니다"),
    MISSING_REQUIRED_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다"),
    UNSUPPORTED_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다"),

    ALREADY_EXISTS_EXCHANGE(HttpStatus.BAD_REQUEST, "이미 존재하는 환율 도메인입니다."),
    ALREADY_EXISTS_ROUND(HttpStatus.BAD_REQUEST, "이미 라운드 설정이 되어 있습니다."),
    INVALID_CURRENCY_CODE(HttpStatus.BAD_REQUEST, "통화 코드가 유효하지 않습니다."),
    INVALID_FETCH_REQUEST(HttpStatus.TOO_EARLY, "가장 최신 회차입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
} 