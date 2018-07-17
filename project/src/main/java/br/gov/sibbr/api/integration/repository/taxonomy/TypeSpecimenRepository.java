package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.TypeSpecimen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeSpecimenRepository extends CrudRepository<TypeSpecimen, Long> {
}
