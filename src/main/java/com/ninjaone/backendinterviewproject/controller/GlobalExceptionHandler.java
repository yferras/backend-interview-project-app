package com.ninjaone.backendinterviewproject.controller;

import com.ninjaone.backendinterviewproject.common.exception.IllegalDataCombinationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends com.ninjaone.backendinterviewproject.common.GlobalExceptionHandler {

    @ExceptionHandler(value = IllegalDataCombinationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String onIllegalDataCombination(IllegalDataCombinationException exception) {
        log.error("Duplicated Value Exception!!!", exception);
        return exception.getMessage();
    }

}
