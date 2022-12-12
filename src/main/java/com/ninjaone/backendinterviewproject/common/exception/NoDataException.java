package com.ninjaone.backendinterviewproject.common.exception;

public class NoDataException extends BusinessRuntimeException{

    public NoDataException(String message, Exception e) {
        super(message, e);
    }
}
