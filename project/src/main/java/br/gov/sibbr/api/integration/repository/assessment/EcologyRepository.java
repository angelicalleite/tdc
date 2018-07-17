package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Ecology;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("assessmentEcologyRepository")
public interface EcologyRepository extends CrudRepository<Ecology, Long> {
}
