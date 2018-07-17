package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.Taxonomy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaxonomyRespository extends CrudRepository<Taxonomy, Long> {

    List<Taxonomy> findAll();

    List<Taxonomy> findByScientificName(String scientificName);

}