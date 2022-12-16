package com.ninjaone.backendinterviewproject.common.validation;

import lombok.Getter;
import lombok.experimental.Accessors;

public abstract class AbstractValidation<T> implements IValidation<T> {

    @Accessors(fluent = true)
    @Getter
    protected final boolean evalNext;

    @Getter
    protected String message;

    /**
     * Constructor.
     *
     * @param evalNext if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                 (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     */
    protected AbstractValidation(boolean evalNext) {
        this.evalNext = evalNext;
    }


}
