package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.Publication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("assessmentPublicationRepository")
public interface PublicationRepository extends CrudRepository<Publication, Long> {
}
