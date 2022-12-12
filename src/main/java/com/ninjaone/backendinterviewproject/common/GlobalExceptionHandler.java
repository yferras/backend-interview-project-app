package com.ninjaone.backendinterviewproject.common;

import com.ninjaone.backendinterviewproject.common.exception.DuplicatedValueException;
import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.common.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@Slf4j
public class GlobalExceptionHandler {

    public static final String NPE = "NPE!!!";

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> onNullPointerException(NullPointerException exception) {
        log.error(NPE, exception);
        return new ResponseEntity<>(NPE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = DuplicatedValueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String onDuplicatedValueException(DuplicatedValueException exception) {
        log.error("Duplicated Value Exception!!!", exception);
        return exception.getMessage();
    }

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public Map<String, Object> onValidationException(ValidationException exception) {
        log.error("Validation Exception!!!", exception);
        return exception.getMap();
    }

    @ExceptionHandler(value = NoDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String onNoDataException(NoDataException exception) {
        log.error("No Data Exception!!!", exception);
        return exception.getMessage();
    }
}
