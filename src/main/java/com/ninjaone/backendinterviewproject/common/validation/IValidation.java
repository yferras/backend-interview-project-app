package com.ninjaone.backendinterviewproject.common.validation;

import lombok.NonNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Top interface for validation API.
 *
 * @param <T> any type.
 */
public interface IValidation<T> {

    /**
     * <p>
     * Test the programmed condition.
     * </p>
     * <p>
     * The intention is that the condition test when the object is invalid.
     * </p>
     *
     * @param obj subject under test.
     * @return returns {@code true} if and only if the tested subject is invalid; otherwise returns {@code false}.
     */
    boolean testCondition(T obj);

    /**
     * <p>
     * Gets the validation message as single string.
     * </p>
     * <p>
     * If more than one issue is found, it is recommended the use of a separator (usually a non-conflicted like: &).
     * </p>
     *
     * @return the validation message.
     */
    @NonNull String getMessage();

    /**
     * Flag used to eval the next validation or not. By default, returns {@code true}.
     *
     * @return returns {@code true} if the next instance of validation must be evaluated.
     */
    default boolean evalNext() {
        return true;
    }


    /**
     * Creates an instance of {@link IValidation} given the predicate.
     *
     * @param predicate         an instance of {@link Predicate}.
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @param <T>               any type.
     * @return an instance of {@link IValidation}
     */
    static <T> IValidation<T> of(Predicate<T> predicate, String validationMessage, boolean evalNext) {
        return new Default<>(predicate, validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} given the predicate (over the result of the
     * mapper.apply).
     *
     * @param predicate         an instance of {@link Predicate}.
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @param <T>               any type, input.
     * @param <R>               any type, output.
     * @return an instance of {@link IValidation}
     */
    static <T, R> IValidation<T> of(Function<T, R> mapper, Predicate<R> predicate, String validationMessage, boolean evalNext) {
        return new Default<>(t -> predicate.test(mapper.apply(t)), validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} that validates a non-null condition.
     *
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @param <T>               any type.
     * @return an instance of {@link IValidation}
     */
    static <T> IValidation<T> requireNonNull(@NonNull String validationMessage, boolean evalNext) {
        return of(Objects::isNull, validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} that validates a non-null condition (over the result of the
     * mapper.apply).
     *
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @param <T>               any type, input.
     * @param <R>               any type, output.
     * @return an instance of {@link IValidation}
     */
    static <T, R> IValidation<T> requireNonNull(Function<T, R> mapper, @NonNull String validationMessage, boolean evalNext) {
        return of(t -> Objects.isNull(mapper.apply(t)), validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} that validates a non-empty string condition.
     *
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @return an instance of {@link IValidation}
     */
    static IValidation<String> requireNonEmpty(@NonNull String validationMessage, boolean evalNext) {
        return new Default<>(String::isEmpty, validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} that validates a non-empty string condition (over the result of the
     * mapper.apply).
     *
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @return an instance of {@link IValidation}
     */
    static <T> IValidation<T> requireNonEmpty(Function<T, String> mapper, @NonNull String validationMessage, boolean evalNext) {
        return new Default<>(t -> mapper.apply(t).isEmpty(), validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} that validates a non-blank string condition.
     *
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @return an instance of {@link IValidation}
     * @throws NullPointerException if the returned String is null.
     */
    static IValidation<String> requireNonBlank(@NonNull String validationMessage, boolean evalNext) {
        return new Default<>(String::isBlank, validationMessage, evalNext);
    }

    /**
     * Creates an instance of {@link IValidation} that validates a non-blank string condition (over the result of the
     * mapper.apply).
     *
     * @param validationMessage the message.
     * @param evalNext          if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                          (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     * @return an instance of {@link IValidation}
     * @throws NullPointerException if the returned String is null.
     */
    static <T> IValidation<T> requireNonBlank(Function<T, String> mapper, @NonNull String validationMessage, boolean evalNext) {
        return new Default<>(t -> mapper.apply(t).isBlank(), validationMessage, evalNext);
    }


}
