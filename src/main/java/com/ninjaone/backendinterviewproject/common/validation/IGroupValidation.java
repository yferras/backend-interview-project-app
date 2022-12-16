package com.ninjaone.backendinterviewproject.common.validation;

import com.ninjaone.backendinterviewproject.common.exception.ValidationException;
import lombok.NonNull;

import java.util.*;

/**
 * Top interface for validation groups API.
 *
 * @param <T> any type.
 */
public interface IGroupValidation<T> extends IValidation<T>, Iterable<IValidation<T>> {

    String getName();

    /**
     * Gets the validation messages (in collection form).
     *
     * @return an instance of {@link Collection} containing all issues description.
     */
    @NonNull Collection<String> getMessages();

    /**
     * Puts the new validation in the list.
     *
     * @param validation instance of {@link IValidation}.
     * @return the instance of the actual group, so you can use this method chained.
     */
    @NonNull IGroupValidation<T> addValidation(IValidation<T> validation);

    boolean isEmpty();

    /**
     * String that acts as marker to split the string returned by: {@link IValidation#getMessage()}
     *
     * @return the separator " & ".
     */
    @NonNull
    default String getSeparator() {
        return " & ";
    }

    /**
     * Checks for validation issues, each group.
     *
     * @param obj    object to test.
     * @param groups iterable of instances of {@link IGroupValidation}.
     * @param <T>    any type.
     * @throws ValidationException if any validation fails.
     */
    static <T, G extends IGroupValidation<T>> void checkGroups(T obj, Iterable<G> groups)
            throws ValidationException {
        Map<String, Object> map = new HashMap<>();
        for (G group : groups) {
            List<String> validationMessages = getValidationMessages(obj, group);
            if (!validationMessages.isEmpty()) {
                map.put(group.getName(), validationMessages);
            }
        }
        if (!map.isEmpty()) {
            map.put("type", obj == null ? "NULL" : obj.getClass().getSimpleName());
            map.put("value", obj);
            throw new ValidationException(map);
        }
    }

    /**
     * Checks for validation issues, each instance of {@link IValidation}.
     *
     * @param obj    object to test.
     * @param validations iterable of instances of {@link IValidation}.
     * @param <T>    any type.
     * @throws ValidationException if any validation fails.
     */
    static <T> void checkValidations(T obj, Iterable<IValidation<T>> validations)
            throws ValidationException {
        List<String> validationMessages = getValidationMessages(obj, validations);
        if (!validationMessages.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", obj == null ? "NULL" : obj.getClass().getSimpleName());
            map.put("value", obj);
            throw new ValidationException(map);
        }
    }

    private static <T> List<String> getValidationMessages(T obj, Iterable<IValidation<T>> validations) {
        List<String> validationMessages = new ArrayList<>(0);
        for (IValidation<T> validation : validations) {
            if (validation.testCondition(obj)) {
                validationMessages.add(validation.getMessage());
                if (!validation.evalNext()) {
                    break;
                }
            }
        }
        return validationMessages;
    }

    /**
     * Creates instances of {@link Group}.
     *
     * @param name     the name of the group.
     * @param evalNext if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                 (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     */
    static <T> IGroupValidation<T> of(String name, boolean evalNext) {
        return new Group<>(name, evalNext);
    }


}
