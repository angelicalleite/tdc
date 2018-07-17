package br.gov.sibbr.api.core.entity;

import java.io.Serializable;

/**
 * Provider information for implementation entity
 */
public interface InterfaceEntity<I extends Serializable> {

    void setId(I id);

    I getId();

}