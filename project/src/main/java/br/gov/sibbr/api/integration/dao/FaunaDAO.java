package br.gov.sibbr.api.integration.dao;

import br.gov.sibbr.api.integration.dto.taxonomy.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FaunaDAO {

    @Autowired
    protected JdbcTemplate jdbcSecundary;

    //todo: verifica quantidade de nomeclatural não nulos, status vazios, ranks vazios, batem com o da base bruta, parent

    public List<TaxonomyDTO> findTaxonsScientificNameStatus() {
        String sql = "SELECT nome_completo, d.rank, sinonimo, d.qualificador, d.taxon_id , autor "
                + "FROM fauna.data d FULL JOIN fauna.taxon t ON d.taxon_id = t.taxon_id ";

        RowMapper<TaxonomyDTO> taxon = (rs, row) -> TaxonomyDTO.builder()
                .scientificName(rs.getString(1))
                .kingdom("Animalia")
                .rank(rs.getString(2))
                .status(rs.getBoolean(3) ? "synonym" : "accept")
                .nomeclaturalStatus(rs.getString(4))
                .taxonId(rs.getLong(5))
                .author(rs.getString(6))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<TaxonomyDTO> findTaxonsRanks() {
        String sql = "SELECT taxon_id, rank FROM fauna.data";

        RowMapper<TaxonomyDTO> taxon = (rs, row) -> TaxonomyDTO.builder()
                .taxonId(rs.getLong(1))
                .rank(rs.getString(2))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<ParentDTO> findTaxonsParent() {
        String sql = "SELECT taxon_id, taxon_pai_id FROM fauna.data";

        RowMapper<ParentDTO> taxon = (rs, row) -> ParentDTO.builder()
                .taxonId(rs.getLong(1))
                .parentId(rs.getLong(2))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<AuthorDTO> findTaxonsAuthorship() {
        String sql = "SELECT taxon_id, autor, nome_completo FROM fauna.data "
                + "WHERE data.autor <> '' ORDER BY nome_completo ";

        RowMapper<AuthorDTO> taxons = (rs, row) -> AuthorDTO.builder()
                .taxonId(rs.getLong(1))
                .authirship(rs.getString(2))
                .build();

        return this.jdbcSecundary.query(sql, taxons);
    }

    public List<SynonymDTO> findTaxonSynonym() {
        String sql = "SELECT relationship_of_resource, related_resource_id, taxon_id FROM fauna.synonym";

        RowMapper<SynonymDTO> taxon = (rs, row) -> SynonymDTO.builder()
                .relationship(rs.getString(1))
                .acceptedTxn(rs.getLong(2))
                .taxonId(rs.getLong(3))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<SpecieProfileDTO> findTaxonsSpecieProfile() {
        String sql = "SELECT habitat, life_form, taxon_id FROM fauna.life_substrate";

        RowMapper<SpecieProfileDTO> lifes = (rs, row) -> SpecieProfileDTO.builder()
                .habitat(rs.getString(1))
                .lifeForm(rs.getString(2))
                .taxonId(rs.getLong(3))
                .build();

        return this.jdbcSecundary.query(sql, lifes);
    }

    public List<PublicationDTO> findTaxonsPublication() {
        String sql = "SELECT taxon_id, bibliographic_citation, name_published_in, name_published_in_year, \"references\" FROM fauna.taxon";

        RowMapper<PublicationDTO> lifes = (rs, row) -> PublicationDTO.builder()
                .taxonId(rs.getLong(1))
                .bibliographicCitation(rs.getString(2))
                .namePublishedIn(rs.getString(3))
                .namePublishedInYear(rs.getString(4))
                .reference(rs.getString(5))
                .build();

        return this.jdbcSecundary.query(sql, lifes);
    }

    public List<DistributionDTO> findTaxonsDistribuition() {
        String sql = "SELECT t.taxon_id, country_code, environment, epicontinental_domain, "
                + "establishment_means, locality, marine_domain, endemic_brazil "
                + "FROM fauna.distribution d INNER JOIN fauna.taxon t ON d.taxon_id=t.taxon_id";

        RowMapper<DistributionDTO> distributions = (rs, row) -> DistributionDTO.builder()
                .taxonId(rs.getLong(1))
                .countryCode(rs.getString(2))
                .environment(rs.getString(3))
                .domain(rs.getString(4))
                .establishmentMeans(rs.getString(5))
                .locality(rs.getString(6))
                .marineDomain(rs.getString(7))
                .endemicBrazil(rs.getBoolean(8))
                .build();

        return this.jdbcSecundary.query(sql, distributions);
    }

    public List<VernacularDTO> findTaxonsVernacularName() {
        String sql = "SELECT unnest(string_to_array(trim(vernacular_name), ', ')) AS name, language, locality, taxon_id "
                + "FROM fauna.name_vernacular";

        RowMapper<VernacularDTO> vernaculares = (rs, row) -> VernacularDTO.builder()
                .name(StringUtils.capitalize(rs.getString(1)).trim())
                .language(rs.getString(2))
                .locality(rs.getString(3))
                .taxonId(rs.getLong(4))
                .build();

        return this.jdbcSecundary.query(sql, vernaculares);
    }

    public List<HierarchyDTO> findTaxonsHierarchy() {
        String sql = "SELECT taxon_id, higher_classification, genus, familia, class, phylum, \"order\" FROM fauna.taxon";

        RowMapper<HierarchyDTO> hierarchys = (rs, row) -> HierarchyDTO.builder()
                .taxonId(rs.getLong(1))
                .kyngdom("Animalia")
                .genus(rs.getString(3))
                .family(rs.getString(4))
                .classe(rs.getString(5))
                .phylum(rs.getString(6))
                .order(rs.getString(7))
                .higherClassification(rs.getString(2))
                .build();

        return this.jdbcSecundary.query(sql, hierarchys);
    }
}
