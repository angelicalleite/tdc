package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.EconomicValue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EconomicValueRepository extends CrudRepository<EconomicValue, Long> {
}
