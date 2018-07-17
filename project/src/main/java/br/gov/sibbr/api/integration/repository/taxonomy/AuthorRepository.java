package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

    Author findByName(String name);
}
