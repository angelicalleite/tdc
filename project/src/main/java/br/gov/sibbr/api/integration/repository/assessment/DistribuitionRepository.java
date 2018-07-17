package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Distribuition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("assessmentDistribuitionRepository")
public interface DistribuitionRepository extends CrudRepository<Distribuition, Long> {
}
