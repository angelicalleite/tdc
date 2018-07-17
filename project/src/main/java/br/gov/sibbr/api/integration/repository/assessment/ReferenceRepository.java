package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Reference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("assessmentReferenceRepository")
public interface ReferenceRepository extends CrudRepository<Reference, Long> {
}
