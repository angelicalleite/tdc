package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.Semantic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemanticRepository extends CrudRepository<Semantic, Long> {

     Semantic findByTxn_Txn(Long txn);
}
