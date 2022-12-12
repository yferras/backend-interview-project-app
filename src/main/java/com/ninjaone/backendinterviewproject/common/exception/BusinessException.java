package com.ninjaone.backendinterviewproject.common.exception;


/**
 * Customized Exception for the business.
 */
public class BusinessException extends Exception {

    public static final String DEF_MESSAGE = "An internal error was produced. Please be patient!!!";
    public BusinessException(Exception e) {
        super(DEF_MESSAGE, e);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Exception e) {
        super(message, e);
    }
}
