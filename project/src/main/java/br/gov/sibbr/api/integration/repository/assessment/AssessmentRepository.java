package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Assessment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends CrudRepository<Assessment, Long> {

    List<Assessment> findByCanonicalNameIsNull();

    List<Assessment> findByTaxonomyIsNull();

    Assessment findFirstByScientificNameIgnoreCaseOrCanonicalNameIgnoreCase(String scientificName, String canonicalName);

}
