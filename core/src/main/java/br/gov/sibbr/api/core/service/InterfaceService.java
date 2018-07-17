package br.gov.sibbr.api.core.service;

import br.gov.sibbr.api.core.entity.InterfaceEntity;
import br.gov.sibbr.api.core.http.RestResponse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * Interface base for layer service
 */
public interface InterfaceService<T extends InterfaceEntity<? extends Serializable>> extends RestResponse {

    Repository<T, Long> getRepository();

    default boolean exists(T entity) {
        return entity.getId() != null && ((CrudRepository<T, Long>) this.getRepository()).exists((Long) entity.getId());
    }

}
