package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.Hierarchy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HierarchyRepository extends CrudRepository<Hierarchy, Long> {

    List<Hierarchy> findByHigherTxnAndType(Long higherTxn, String type);

}
