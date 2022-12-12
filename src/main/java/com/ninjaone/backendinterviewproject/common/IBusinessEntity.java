package com.ninjaone.backendinterviewproject.common;

import java.io.Serializable;

/**
 * Interface to mark the entities.
 *
 * @param <I>
 */
public interface IBusinessEntity<I extends Serializable> {

    /**
     * Gets the identifier.
     *
     * @return the identifier.
     */
    I getId();

    /**
     * Sets the identifier.
     *
     * @param id  the identifier.
     */
    void setId(I id);
}
