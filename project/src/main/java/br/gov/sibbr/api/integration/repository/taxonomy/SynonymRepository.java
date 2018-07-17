package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.Synonym;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SynonymRepository extends CrudRepository<Synonym, Long> {

}

