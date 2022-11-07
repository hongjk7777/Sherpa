package com.sherpa.carrier_sherpa.domain.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    // 일반 Exception은 orElseThrow 통과 못함 -> RuntimeException
    private final ErrorCode errorCode;
    private final String message;

    public BaseException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
