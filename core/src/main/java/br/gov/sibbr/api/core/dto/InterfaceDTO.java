package br.gov.sibbr.api.core.dto;

/**
 * Interface for implementation converter entity and dto
 */
public interface InterfaceDTO<T, R> {

    R toDto(T entity);

    T toEntity(R dto);
}