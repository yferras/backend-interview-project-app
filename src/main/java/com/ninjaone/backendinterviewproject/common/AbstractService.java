package com.ninjaone.backendinterviewproject.common;

import com.ninjaone.backendinterviewproject.common.converter.IConverter;
import com.ninjaone.backendinterviewproject.common.exception.BusinessException;
import com.ninjaone.backendinterviewproject.common.exception.DuplicatedValueException;
import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.common.validation.DtoValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Business top of abstraction.
 *
 * @param <I> any serializable type.
 * @param <D> any class derived from {@link IBusinessDto}.
 * @param <E> any class derived from {@link IBusinessEntity}.
 */
@AllArgsConstructor
@Slf4j
public abstract class AbstractService<I extends Serializable, D extends IBusinessDto, E extends IBusinessEntity<I>>
        implements IService<I, D> {

    protected final IConverter<E, D> converter;
    protected final JpaRepository<E, I> repository;
    protected final List<DtoValidation.Group<D>> validationGroupsBeforeInsert = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public D addNew(D dto) throws BusinessException {
        DtoValidation.checkGroups(dto, validationGroupsBeforeInsert);
        log.info("D::{} is valid!!!", converter.getDtoClass().getSimpleName());
        try {
            E entity = converter.convertToEntity(dto);
            entity.setId(null); // Only inserts; no modify.
            entity = repository.save(entity);
            log.info("New E::{} was inserted: ID({})", converter.getEntityClass().getSimpleName(), entity.getId());
            return converter.convertToDto(entity);
        } catch (DataIntegrityViolationException e) {
            // converter has the entity class.
            throw new DuplicatedValueException(converter.getEntityClass(), e);
        }
        catch (Exception e) {
            throw new BusinessException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(I id) throws BusinessException {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            String message = MessageFormat.format("E::{0}(ID = {1,number,#}) Not found.",
                    converter.getEntityClass().getSimpleName(),
                    id
            );
            throw new NoDataException(message, e);
        }
        catch (Exception e) {
            throw new BusinessException(e);
        }
    }

    @Override
    public Optional<D> getById(I id) {
        return repository.findById(id).map(converter::convertToDto);
    }


}
