package com.ninjaone.backendinterviewproject.common.converter;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.IBusinessEntity;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * Top abstraction to create converters between entities and DTOs
 *
 * @param <E> any class derived from {@link IBusinessEntity}
 * @param <D> any class derived from {@link IBusinessDto}
 */
@Slf4j
@Getter
public abstract class AbstractConverter<E extends IBusinessEntity<? extends Serializable>, D extends IBusinessDto>
        implements IConverter<E, D> {
    /**
     * For logging purposes.
     */
    private final Class<E> entityClass;

    /**
     * For logging purposes.
     */
    private final Class<D> dtoClass;


    protected AbstractConverter() {
        entityClass = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        dtoClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        log.info("A new bidirectional converter was created; [E::{} -> D::{}]",
                entityClass.getSimpleName(),
                dtoClass.getSimpleName()
        );
    }
}
