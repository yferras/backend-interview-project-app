package com.ninjaone.backendinterviewproject.common.validation;

/**
 * Top interface for validation API.
 *
 * @param <T> any type.
 */
public interface IValidation<T> {

    /**
     * Test the programmed condition.
     *
     * @param obj input to test.
     * @return returns {@code true} if and only if the given condition is true; otherwise returns {@code false}.
     */
    boolean testCondition(T obj);

    /**
     * Gets the validation message.
     *
     * @return the validation message.
     */
    String getMessage();

    /**
     * Flag used to eval the next validation or not.
     *
     * @return returns {@code true} if the next instance of validation must be evaluated.
     */
    boolean evalNext();
}
