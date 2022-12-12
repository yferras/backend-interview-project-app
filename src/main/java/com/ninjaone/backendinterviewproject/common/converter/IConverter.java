package com.ninjaone.backendinterviewproject.common.converter;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.IBusinessEntity;

import java.io.Serializable;

/**
 * Top abstraction to create converters between entities and DTOs
 *
 * @param <E> any class derived from {@link IBusinessEntity}
 * @param <D> any class derived from {@link IBusinessDto}
 */
public interface IConverter<E extends IBusinessEntity<? extends Serializable>, D extends IBusinessDto> {

    /**
     * Converts to DTO the given entity.
     *
     * @param entity instance of entity.
     * @return instance of DTO.
     */
    D convertToDto(E entity);

    /**
     * Converts to entity from the given DTO.
     *
     * @param dto instance of DTO.
     * @return instance of entity.
     */
    E convertToEntity(D dto);

    /**
     * Gets the class of the entity instance.
     *
     * @return an instance of {@link Class}
     */
    Class<E> getEntityClass();

    /**
     * Gets the class of the DTO instance.
     *
     * @return an instance of {@link Class}
     */
    Class<D> getDtoClass();

}
