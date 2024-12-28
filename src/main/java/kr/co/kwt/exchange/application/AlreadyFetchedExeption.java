package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.config.error.BusinessException;
import kr.co.kwt.exchange.config.error.ErrorCode;

public class AlreadyFetchedExeption extends BusinessException {

    public AlreadyFetchedExeption() {
        super(ErrorCode.ALREADY_FETCHED);
    }

    public AlreadyFetchedExeption(ErrorCode errorCode) {
        super(errorCode);
    }

    public AlreadyFetchedExeption(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
