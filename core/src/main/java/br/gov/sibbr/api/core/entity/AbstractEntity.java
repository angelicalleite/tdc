package br.gov.sibbr.api.core.entity;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractEntity<I extends Serializable> implements InterfaceEntity<I> {

    private I id;

    @Override
    public I getId() {
        return this.id;
    }

    public void setId(I id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AbstractEntity<?> that = (AbstractEntity<?>) obj;

        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
