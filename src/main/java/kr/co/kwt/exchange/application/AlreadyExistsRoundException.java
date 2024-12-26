package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.config.error.BusinessException;
import kr.co.kwt.exchange.config.error.ErrorCode;

public class AlreadyExistsRoundException extends BusinessException {

    public AlreadyExistsRoundException() {
        super(ErrorCode.ALREADY_EXISTS_ROUND);
    }

    public AlreadyExistsRoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlreadyExistsRoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
