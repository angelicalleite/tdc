package br.gov.sibbr.api.integration.repository.media;

import br.gov.sibbr.api.integration.entity.assessment.Action;
import br.gov.sibbr.api.integration.entity.media.Media;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends CrudRepository<Media, Long> {
}
