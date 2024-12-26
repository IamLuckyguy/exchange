package kr.co.kwt.exchange.adapter.in;

import kr.co.kwt.exchange.config.error.BusinessException;
import kr.co.kwt.exchange.config.error.ErrorCode;

public class InvalidFetchRequest extends BusinessException {

    public InvalidFetchRequest() {
        super(ErrorCode.INVALID_FETCH_REQUEST);
    }

    public InvalidFetchRequest(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidFetchRequest(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
