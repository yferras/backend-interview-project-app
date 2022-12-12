package com.ninjaone.backendinterviewproject.common;

import com.ninjaone.backendinterviewproject.common.exception.BusinessException;

import java.io.Serializable;
import java.util.Optional;

/**
 * Business top of abstraction.
 *
 * @param <I> any serializable type.
 * @param <D> any class derived from {@link IBusinessDto}.
 */
public interface IService<I extends Serializable, D extends IBusinessDto> {

    /**
     * Adds new data to the database.
     *
     * @param dto the input data.
     * @return the instance updated. E.g. the identifier.
     */
    D addNew(D dto) throws BusinessException;

    /**
     * Deletes the entity in the database by its identifier.
     *
     * @param id identifier.
     */
    void deleteById(I id) throws BusinessException;

    /**
     * Get the DTO by its identifier.
     *
     * @param id identifier.
     * @return an optional containing or not the instance of the DTO.
     */
    Optional<D> getById(I id);
}
