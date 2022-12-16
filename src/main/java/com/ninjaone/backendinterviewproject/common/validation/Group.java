package com.ninjaone.backendinterviewproject.common.validation;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;


/**
 * A class to group instances of {@link IValidation} (generally validations related to a concept or a category).
 *
 * @param <T> any type.
 */
class Group<T> extends AbstractValidation<T> implements IGroupValidation<T>, Iterable<IValidation<T>> {

    private final List<IValidation<T>> validations = new ArrayList<>(0);

    private final List<String> messages = new ArrayList<>(0);

    @Getter
    private final String name;

    /**
     * Creates instances of {@link Group}.
     *
     * @param name     the name of the group.
     * @param evalNext if the next validation will be evaluated (<code>evalNext = true</code>) or not
     *                 (<code>evalNext = false</code>) if in this group at least one validation is {@code true}.
     */
    public Group(String name, boolean evalNext) {
        super(evalNext);
        this.name = name;
    }

    /**
     * Creates instances of {@link Group} that allows the next validation to be evaluated.
     *
     * @param name the name of the group.
     */
    public Group(String name) {
        this(name, true);
    }

    @Override
    public @NonNull Collection<String> getMessages() {
        return Collections.unmodifiableCollection(messages);
    }

    public Group<T> addValidation(IValidation<T> validation) {
        validations.add(validation);
        return this;
    }

    @Override
    public boolean isEmpty() {
        return validations.isEmpty();
    }

    @Override
    public Iterator<IValidation<T>> iterator() {
        return validations.iterator();
    }

    @Override
    public boolean testCondition(T obj) {
        messages.clear();
        for (IValidation<T> validation : validations) {
            if (validation.testCondition(obj)) {
                messages.add(validation.getMessage());
                if (!validation.evalNext()) {
                    break;
                }
            }
        }
        return !messages.isEmpty();
    }

    /**
     * {@inheritDoc}
     * <p>
     * The message will be in this format:
     * </p>
     * <p>
     * "Issue description 1 & Issue description 2 & ... & Issue description N"
     * </p>
     */
    @Override
    public String getMessage() {
        return String.join(getSeparator(), messages);
    }

}
