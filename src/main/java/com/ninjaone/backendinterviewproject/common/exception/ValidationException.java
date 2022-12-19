package com.ninjaone.backendinterviewproject.common.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Exception for validation errors.
 */
public class ValidationException extends BusinessRuntimeException {


    @Getter
    private final transient Map<String, Object> map;

    public ValidationException(Map<String, Object> map) {
        super("Validation issues.");
        this.map = map;
    }

}
