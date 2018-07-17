package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Vernacular;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("assessmentVernacularRepository")
public interface VernacularRepository extends CrudRepository<Vernacular, Long> {
}
