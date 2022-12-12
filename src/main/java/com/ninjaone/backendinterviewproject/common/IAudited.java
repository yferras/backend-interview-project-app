package com.ninjaone.backendinterviewproject.common;

import java.util.Date;

/**
 * Interface for the audition process.
 */
public interface IAudited {

    /**
     * Gets the date and time when the entity was created.
     *
     * @return an instance of {@link Date}
     */
    Date getCreatedAt();

    /**
     * Sets the date and time when the entity was created.
     *
     * @param date an instance of {@link Date}
     */
    void setCreatedAt(Date date);

    /**
     * Gets the date and time when the entity was modified.
     *
     * @return an instance of {@link Date}
     */
    Date getLastModified();

    /**
     * Sets the date and time when the entity was modified.
     *
     * @param date an instance of {@link Date}
     */
    void setLastModified(Date date);

}
