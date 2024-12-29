package kr.co.kwt.exchange.application;

import kr.co.kwt.exchange.config.error.ErrorCode;

public class ServerException extends BusinessException {

    public ServerException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public ServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}
