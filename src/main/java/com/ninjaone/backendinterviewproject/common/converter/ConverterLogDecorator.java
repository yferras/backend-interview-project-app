package com.ninjaone.backendinterviewproject.common.converter;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.IBusinessEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * Only prints the logs when all goes all right.
 *
 * @param <E> any class derived from {@link IBusinessEntity}
 * @param <D> any class derived from {@link IBusinessDto}
 */
@Slf4j
public class ConverterLogDecorator<E extends IBusinessEntity<? extends Serializable>, D extends IBusinessDto>
        extends AbstractConverterDecorator<E, D> {
    public static final String NAME = "logger-dec";

    public ConverterLogDecorator() {
        super(NAME);
    }

    @Override
    protected D toDto(E entity) {
        D dto = getConverter().convertToDto(entity);
        log.info("An instance of: E::{} was converted successfully to an instance of: D::{}",
                getEntityClass().getSimpleName(),
                getDtoClass().getSimpleName()
        );
        return dto;
    }

    @Override
    protected E toEntity(D dto) {
        E entity = getConverter().convertToEntity(dto);
        log.info("An instance of: D::{} was converted successfully to an instance of: E::{}",
                getDtoClass().getSimpleName(),
                getEntityClass().getSimpleName()
        );
        return entity;
    }
}
