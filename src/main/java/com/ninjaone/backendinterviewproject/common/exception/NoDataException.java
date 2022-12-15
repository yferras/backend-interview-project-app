package com.ninjaone.backendinterviewproject.common.exception;

public class NoDataException extends BusinessRuntimeException {
    public static final String NOT_FOUND_TEMPLATE = "E::{0}({1}) Not found.";

    public NoDataException(String message, Exception e) {
        super(message, e);
    }

    public NoDataException(String message) {
        this(message, null);
    }

}
