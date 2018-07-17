package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Action;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends CrudRepository<Action, Long> {
}
