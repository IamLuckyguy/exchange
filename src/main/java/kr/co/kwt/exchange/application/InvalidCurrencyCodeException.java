package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.config.error.ErrorCode;

public class InvalidCurrencyCodeException extends BusinessException {

    public InvalidCurrencyCodeException() {
        super(ErrorCode.INVALID_CURRENCY_CODE);
    }

    public InvalidCurrencyCodeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidCurrencyCodeException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
