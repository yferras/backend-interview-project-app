package com.ninjaone.backendinterviewproject.common.exception;

import com.ninjaone.backendinterviewproject.common.IBusinessEntity;

import java.io.Serializable;
import java.text.MessageFormat;

public class DuplicatedValueException extends BusinessRuntimeException {

    public static final String MESSAGE_TEMPLATE = "Data duplication [{0}].";
    public DuplicatedValueException(Exception e) {
        super(e);
    }

    public DuplicatedValueException(Class<? extends IBusinessEntity<? extends Serializable>> businessEntityClass) {
        super(MessageFormat.format(MESSAGE_TEMPLATE, businessEntityClass.getSimpleName()));
    }


    public DuplicatedValueException(Class<? extends IBusinessEntity<? extends Serializable>> businessEntityClass, Exception e) {
        super(MessageFormat.format(MESSAGE_TEMPLATE, businessEntityClass.getSimpleName()), e);
    }
}
