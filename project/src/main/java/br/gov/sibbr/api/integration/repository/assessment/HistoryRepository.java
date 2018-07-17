package br.gov.sibbr.api.integration.repository.assessment;

import br.gov.sibbr.api.integration.entity.assessment.History;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HistoryRepository extends CrudRepository<History, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE assessment.history SET assessment_id=:novo WHERE assessment_id=:atual", nativeQuery = true)
    int updateAssessmentId(@Param("novo") Long novo, @Param("atual") Long atual);

}
