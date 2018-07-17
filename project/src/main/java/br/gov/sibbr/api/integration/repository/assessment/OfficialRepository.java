package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Official;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficialRepository extends CrudRepository<Official, Long> {

    Official getByName(String official);
}
