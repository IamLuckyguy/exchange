package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.config.error.BusinessException;
import kr.co.kwt.exchange.config.error.ErrorCode;

public class AlreadyExistsExchangeException extends BusinessException {

    public AlreadyExistsExchangeException() {
        super(ErrorCode.ALREADY_EXISTS_EXCHANGE);
    }

    public AlreadyExistsExchangeException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlreadyExistsExchangeException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
