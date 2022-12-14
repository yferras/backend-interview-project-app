package com.ninjaone.backendinterviewproject.common.validation;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.exception.ValidationException;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.*;
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

    public static <D extends IBusinessDto> void checkGroups(@NonNull D dto, List<DtoValidation.Group<D>> validationGroups) throws ValidationException {

        Map<String, Object> map = new HashMap<>();
        map.put("type", dto.getClass().getSimpleName());
        map.put("value", dto);
        boolean isInvalid = false;
        for (DtoValidation.Group<D> group : validationGroups) {
            List<String> validationMessages = new ArrayList<>(0);
            for (DtoValidation<D> validation : group) {
                if (validation.testCondition(dto)) {
                    validationMessages.add(validation.getMessage());
                    isInvalid = true;
                    if (!validation.evalNext()) {
                        break;
                    }
                }
            }

            if (!validationMessages.isEmpty()) {
                map.put(group.getName(), validationMessages);
            }
        }
        if (isInvalid) {
            throw new ValidationException(map);
        }
    }

}
