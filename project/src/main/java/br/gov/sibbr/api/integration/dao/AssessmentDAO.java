package br.gov.sibbr.api.integration.dao;

import br.gov.sibbr.api.integration.dto.assessment.*;
import br.gov.sibbr.api.integration.entity.assessment.*;
import br.gov.sibbr.api.integration.entity.taxonomy.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sun.java2d.cmm.Profile;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Repository
public class AssessmentDAO {

    @Autowired
    protected JdbcTemplate jdbcSecundary;

    @Autowired
    protected JdbcTemplate jdbcPrimary;

    public List<AssessmentFaunaDTO> findAssessmentsFauna() {
        String sql = "SELECT category, criteria, complete_name, vulgar_name, source, year "
                + "FROM ameaca_fauna.assessment ";

        RowMapper<AssessmentFaunaDTO> asmt = (rs, row) -> AssessmentFaunaDTO.builder()
                .category(rs.getString(1))
                .criteria(rs.getString(2))
                .scientificName(rs.getString(3))
                .vernacularName(rs.getString(4))
                .source(rs.getString(6))
                .year(rs.getInt(7))
                .officialId(1L)
                .build();

        return this.jdbcSecundary.query(sql, asmt);
    }

    public List<AssessmentFaunaDTO> findAssessmentsFaunaNT() {
        String sql = "SELECT category, scientific_name, vulgar_name, year FROM ameaca_fauna.assessment_nt";

        RowMapper<AssessmentFaunaDTO> asmt = (rs, row) -> AssessmentFaunaDTO.builder()
                .category(rs.getString(1))
                .scientificName(rs.getString(2))
                .vernacularName(rs.getString(3))
                .year(rs.getInt(4))
                .officialId(1L)
                .build();

        return this.jdbcSecundary.query(sql, asmt);
    }

    public List<AssessmentFaunaDTO> findAssessmentsFaunaDD() {
        String sql = "SELECT category, scientific_name, vulgar_name, year FROM ameaca_fauna.assessment_dd";

        RowMapper<AssessmentFaunaDTO> asmt = (rs, row) -> AssessmentFaunaDTO.builder()
                .category(rs.getString(1))
                .scientificName(rs.getString(2))
                .vernacularName(rs.getString(3))
                .year(rs.getInt(4))
                .officialId(1L)
                .build();

        return this.jdbcSecundary.query(sql, asmt);
    }

    public List<AssessmentGlobalDTO> findAssessmentsGlobal() {
        String sql = "SELECT taxon_id, code, h.category, scientific_name, year "
                + "FROM ameaca_global.assessment a FULL JOIN ameaca_global.history h ON a.id = h.assessment_id ";

        RowMapper<AssessmentGlobalDTO> asmt = (rs, row) -> AssessmentGlobalDTO.builder()
                .taxonId(rs.getLong(1))
                .code(rs.getString(2))
                .category(rs.getString(3))
                .scientificName(rs.getString(4))
                .year(rs.getString(5) == null ? null : Integer.parseInt(rs.getString(5)))
                .build();

        return this.jdbcSecundary.query(sql, asmt);
    }

    public List<AssessmentRegionDTO> findAssessmentsRegional() {
        String sql = "SELECT category, scientific_name, a.locality, official_id, a.grupo, a.year, code, o.official "
                + "FROM ameaca_regional.assessment a inner join ameaca_regional.official o ON a.official_id = o.id ";

        RowMapper<AssessmentRegionDTO> asmt = (rs, row) -> AssessmentRegionDTO.builder()
                .category(rs.getString(1))
                .scientificName(rs.getString(2))
                .localy(rs.getString(3))
                .officialId(rs.getLong(4))
                .grupo(rs.getString(5))
                .year(rs.getInt(6))
                .code(rs.getString(7))
                .official(rs.getString(8))
                .build();

        return this.jdbcSecundary.query(sql, asmt);
    }

    public List<AssessmentFloraDTO> findAssessmentsFlora() {
        try {
            String sql = "SELECT a.taxon_id, scientific_name, category, criteria, evaluator, assessor, rationale, "
                    + "a.id, tn.notes, date_of_assessment, a.metadata_id, p.id "
                    + "FROM ameaca_flora.assessment a FULL JOIN ameaca_flora.taxon t ON a.taxon_id = t.taxon_id "
                    + "FULL JOIN ameaca_flora.profile p ON t.taxon_id = p.taxon_id "
                    + "FULL JOIN ameaca_flora.taxonomic_notes tn ON p.taxonomic_notes_id = tn.id ";

            RowMapper<AssessmentFloraDTO> asmt = (rs, row) -> AssessmentFloraDTO.builder()
                    .taxonId(rs.getLong(1))
                    .metadatoId(rs.getLong(11))
                    .profileId(rs.getLong(12))
                    .assessmentId(rs.getLong(8))
                    .scientificName(rs.getString(2))

                    .category(rs.getString(3))
                    .criteria(rs.getString(4))

                    .evaluator(rs.getString(5))
                    .assessor(rs.getString(6))

                    .rationale(rs.getString(7))
                    .notes(rs.getString(9))
                    .dateOfAssessment(Instant.ofEpochMilli(rs.getInt(10)).atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .build();

            return this.jdbcSecundary.query(sql, asmt);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public EconomicValue findAssessmentsFloraEconomicValue(Long profileId, Assessment assessment) {
        try {
            String sql = "SELECT ev.potential_economic_value FROM ameaca_flora.metadata m INNER JOIN ameaca_flora.profile p ON m.id = p.metadata_id "
                    + "INNER JOIN ameaca_flora.economic_value ev ON p.economic_value_id = ev.id WHERE p.id = ? ";

            RowMapper<EconomicValue> ev = (rs, row) -> EconomicValue.builder()
                    .potentialEconomicValue(rs.getString(1))
                    .details("Dados CNCFlora 2014")
                    .assessment(assessment)
                    .build();

            return this.jdbcSecundary.queryForObject(sql, ev, profileId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public List<Action> findAssessmentsFloraAction(Long profileId, History history) {
        try {
            String sql = " SELECT action, details FROM ameaca_flora.action a WHERE profile_id = ? ";

            RowMapper<Action> actions = (rs, row) -> Action.builder()
                    .action(rs.getString(1))
                    .details(rs.getString(2))
                    .history(history)
                    .build();

            return this.jdbcSecundary.query(sql, actions, profileId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Ecology findAssessmentsFloraEcology(Long profileId, Assessment assessment) {
        try {
            String sql = "SELECT "
                    + " string_agg(DISTINCT e.resume, ' | ')                AS resume, "
                    + " string_agg(DISTINCT eb.biomas, ' | ')               AS biomas, "
                    + " string_agg(DISTINCT eh.habitats, ' | ')             AS habitats, "
                    + " string_agg(DISTINCT ef.fitofisionomies, ' | ')      AS fitofisionomies, "
                    + " string_agg(DISTINCT exf.extreme_fluctuation, ' | ') AS extreme_fluctuation, "
                    + " string_agg(DISTINCT d.eoo, ' | ')                   AS eoo, "
                    + " string_agg(DISTINCT d.aoo, ' | ')                   AS aoo, "
                    + " string_agg(DISTINCT d.resume, ' | ')                AS distribuition, "
                    + " string_agg(DISTINCT d.fragmented, ' | ')            AS fragmented, "
                    + " string_agg(DISTINCT d.brasilian_endemic, ' | ')     AS brasilian_endemic, "
                    + " p.id "
                    + "FROM ameaca_flora.profile p inner join ameaca_flora.ecology e ON p.ecology_id = e.id "
                    + " FULL JOIN ameaca_flora.ecology_biomas eb ON e.id = eb.ecology_id "
                    + " FULL JOIN ameaca_flora.ecology_fitofisionomies ef ON e.id = ef.ecology_id "
                    + " FULL JOIN ameaca_flora.ecology_habitats eh ON e.id = eh.ecology_id "
                    + " FULL JOIN ameaca_flora.population pl ON p.population_id = pl.id "
                    + " FULL JOIN ameaca_flora.extreme_fluctuation exf ON pl.extreme_fluctuation_id = exf.id "
                    + " FULL JOIN ameaca_flora.distribution d ON p.distribution_id = d.id "
                    + "WHERE p.id = ? "
                    + "GROUP BY p.id";

            RowMapper<Ecology> ecology = (rs, row) -> Ecology.builder()
                    .resume(rs.getString(1))
                    .biomes(rs.getString(2))
                    .habitats(rs.getString(3))
                    .fitofisionomies(rs.getString(4))
                    .extremeFluctioation(rs.getString(5))
                    .eoo(rs.getString(6))
                    .aoo(rs.getString(7))
                    .distribuition(rs.getString(8))
                    .fragmented(rs.getString(9))
                    .brasilianEndemic(rs.getString(10))
                    .assessment(assessment)
                    .build();

            return this.jdbcSecundary.queryForObject(sql, ecology, profileId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public List<AssessmentDTO> findAssessmentsDuplicate() {
        String sql = "SELECT id, txn, l.canonical_name, l.scientific_name "
                + "FROM assessment.assessment l WHERE canonical_name IN (SELECT b.canonical_name "
                + "FROM assessment.assessment b GROUP BY canonical_name HAVING count(canonical_name) > 1) "
                + "ORDER BY l.canonical_name, l.scientific_name ";

        RowMapper<AssessmentDTO> asmt = (rs, row) -> AssessmentDTO.builder()
                .id(rs.getLong(1))
                .txn(rs.getLong(2))
                .canonicalName(rs.getString(3))
                .scientificName(rs.getString(4))
                .build();

        return this.jdbcPrimary.query(sql, asmt);
    }

    public Assessment findAssessmentsRegionalNotExistBy(String name, Integer year, String distribuition) {
        try {
            String sql = "SELECT a.id, a.scientific_name, a.canonical_name, a.txn FROM assessment.assessment a " +
                    " INNER JOIN assessment.history h ON a.id = h.assessment_id " +
                    " INNER JOIN assessment.distribuition d ON h.id = d.history_id " +
                    "WHERE (a.scientific_name = ? OR a.canonical_name = ?) AND h.year = ? AND d.locality = ?";

            RowMapper<Assessment> asmt = (rs, row) -> Assessment.builder()
                    .id(rs.getLong(1))
                    .scientificName(rs.getString(2))
                    .canonicalName(rs.getString(3))
                    .taxonomy(Taxonomy.builder().txn(rs.getLong(4)).build())
                    .build();

            return this.jdbcPrimary.queryForObject(sql, asmt, name, name, year, distribuition);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public List<String> findAssessmentsDuplicateDistinct() {
        String sql = "SELECT canonical_name FROM assessment.assessment "
                + "GROUP BY canonical_name HAVING count(canonical_name) > 1 ";

        RowMapper<String> asmt = (rs, row) -> rs.getString(1);

        return this.jdbcPrimary.query(sql, asmt);
    }

    public List<Assessment> findAssessmentByName(String name) {
        try {
            String sql = "SELECT id, scientific_name, canonical_name, txn FROM assessment.assessment "
                    + "WHERE lower(scientific_name) = ? OR lower(canonical_name) = ? ";

            RowMapper<Assessment> asmt = (rs, row) -> Assessment.builder()
                    .id(rs.getLong(1))
                    .scientificName(rs.getString(2))
                    .canonicalName(rs.getString(3))
                    .taxonomy(Taxonomy.builder().txn(rs.getLong(4)).build())
                    .build();

            return this.jdbcPrimary.query(sql, asmt, name.toLowerCase(), name.toLowerCase());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Official findOfficialByCode(String code) {
        try {
            String sql = "SELECT id FROM assessment.official WHERE trim(upper(code)) = ? ";

            RowMapper<Official> asmt = (rs, row) -> Official.builder()
                    .id(rs.getLong(1))
                    .build();

            return this.jdbcPrimary.queryForObject(sql, asmt, code.toUpperCase().trim());
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

}
