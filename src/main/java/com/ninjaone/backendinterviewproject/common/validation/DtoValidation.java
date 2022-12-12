package com.ninjaone.backendinterviewproject.common.validation;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Specialization to validate DTO.
 *
 * @param <D> any type derived from {@link IBusinessDto}.
 */

public class DtoValidation<D extends IBusinessDto> extends AbstractValidation<D> {

    /**
     * Constructor.
     *
     * @param condition an instance of {@link Predicate}.
     * @param message   the validation message.
     * @param evalNext  if the next validation must be evaluated.
     */
    public DtoValidation(Predicate<D> condition, String message, boolean evalNext) {
        super(condition, message, evalNext);
    }

    /**
     * Constructor. By default, the next validation will be evaluated.
     *
     * @param condition an instance of {@link Predicate}.
     * @param message   the validation message.
     */
    public DtoValidation(Predicate<D> condition, String message) {
        super(condition, message, true);
    }


    /**
     * Utility class to group validations.
     *
     * @param <D> any type derived from {@link IBusinessDto}.
     */
    public static class Group<D extends IBusinessDto> implements Iterable<DtoValidation<D>> {
        private final List<DtoValidation<D>> validations = new ArrayList<>();

        @Getter
        private final String name;

        /**
         * Constructor.
         *
         * @param name the name of the group.
         */
        public Group(String name) {
            this.name = name;
        }

        /**
         * Puts the instance of {@link DtoValidation} in the group.
         *
         * @param validation instance of {@link DtoValidation}.
         */
        public void addValidation(DtoValidation<D> validation) {
            validations.add(validation);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Iterator<DtoValidation<D>> iterator() {
            return validations.iterator();
        }
    }

}
