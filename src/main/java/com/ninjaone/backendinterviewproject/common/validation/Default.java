package com.ninjaone.backendinterviewproject.common.validation;

import lombok.NonNull;

import java.util.function.Predicate;

/**
 * Default implementation.
 *
 * @param <T> any type.
 */
class Default<T> extends AbstractValidation<T>  {
    private final Predicate<T> condition;

    /**
     * Constructor.
     *
     * @param condition an instance of {@link Predicate}.
     * @param message   the validation message.
     * @param evalNext  if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                  (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     */
    Default(@NonNull  Predicate<T> condition, @NonNull String message, boolean evalNext) {
        super(evalNext);
        this.condition = condition;
        this.message = message;
    }

    @Override
    public boolean testCondition(T obj) {
        return condition.test(obj);
    }
}
