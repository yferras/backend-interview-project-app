package com.ninjaone.backendinterviewproject.common.exception;


/**
 * Customized Exception for the business.
 */
public class BusinessRuntimeException extends RuntimeException {
    public BusinessRuntimeException(String message) {
        super(message);
    }

    public BusinessRuntimeException(Exception e) {
        super(e);
    }

    public BusinessRuntimeException(String message, Exception e) {
        super(message, e);
    }
}
