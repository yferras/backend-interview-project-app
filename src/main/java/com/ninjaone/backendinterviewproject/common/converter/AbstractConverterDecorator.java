package com.ninjaone.backendinterviewproject.common.converter;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.IBusinessEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Top abstraction to create a decorator to the converter.
 *
 * @param <E> any class derived from {@link IBusinessEntity}
 * @param <D> any class derived from {@link IBusinessDto}
 */
@Getter
public abstract class AbstractConverterDecorator<E extends IBusinessEntity<? extends Serializable>, D extends IBusinessDto>
        extends AbstractConverter<E, D> {

    @Setter
    private IConverter<E, D> converter;

    private final String name;

    protected AbstractConverterDecorator(String name) {
        this.name = name;
    }

    /**
     * Performs all validations before execute the conversion.
     *
     * @throws NullPointerException if the converter is null.
     */
    protected void validate() {
        Objects.requireNonNull(getConverter(), "The wrapped converter is null. Use: setConverter to set the instance.");
    }

    @Override
    public D convertToDto(E entity) {
        validate();
        return toDto(entity);
    }

    /**
     * This method is called after validation. Performs the conversion.
     *
     * @param entity instance of entity.
     * @return instance of DTO.
     */
    protected abstract D toDto(E entity);

    @Override
    public E convertToEntity(D dto) {
        validate();
        return toEntity(dto);
    }

    /**
     * This method is called after validation. Performs the conversion.
     *
     * @param dto instance of DTO.
     * @return instance of entity.
     */
    protected abstract E toEntity(D dto);
}
