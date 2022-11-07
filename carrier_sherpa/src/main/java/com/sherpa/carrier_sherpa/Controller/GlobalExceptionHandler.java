package com.sherpa.carrier_sherpa.Controller;

import com.sherpa.carrier_sherpa.domain.exception.BaseException;
import com.sherpa.carrier_sherpa.dto.ExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ExceptionDto testException(BaseException e) {
        GlobalExceptionHandler.log.error("error message", e);
        return new ExceptionDto(e.getErrorCode(),e.getMessage());
    }
}
