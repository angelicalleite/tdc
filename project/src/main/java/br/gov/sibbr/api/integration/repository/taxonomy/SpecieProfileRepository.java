package br.gov.sibbr.api.integration.repository.taxonomy;

import br.gov.sibbr.api.integration.entity.taxonomy.SpeciesProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecieProfileRepository extends CrudRepository<SpeciesProfile, Long> {

    List<SpeciesProfile> findAll();
}