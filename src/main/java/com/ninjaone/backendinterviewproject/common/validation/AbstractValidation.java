package com.ninjaone.backendinterviewproject.common.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Predicate;

/**
 * Top abstraction for validation API.
 *
 * @param <T> any type.
 */
@AllArgsConstructor
public abstract class AbstractValidation<T> implements IValidation<T> {
    private final Predicate<T> condition;

    @Getter
    private final String message;

    private final boolean evalNext;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean testCondition(T dto) {
        return condition.test(dto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean evalNext() {
        return evalNext;
    }
}
