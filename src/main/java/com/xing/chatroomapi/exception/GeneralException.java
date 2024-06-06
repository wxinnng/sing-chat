package com.xing.chatroomapi.exception;

import org.springframework.http.HttpStatus;

/**
 * 异常
 */
public class GeneralException extends RuntimeException {

    private Integer errorCode;

    public GeneralException() {
    }

    public GeneralException(Throwable throwable) {
        super(throwable);
    }

    public GeneralException(String msg) {
        super(msg);
        this.errorCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    public GeneralException(Integer errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }


    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
