package br.gov.sibbr.api.integration.dao;

import br.gov.sibbr.api.integration.dto.taxonomy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FloraDAO {

    @Autowired
    protected JdbcTemplate jdbcSecundary;

    public List<TaxonomyDTO> findTaxonsScientificNameStatus() {
        String sql = "SELECT scientific_name, kingdom, taxon_rank, taxonomic_status, nomenclatural_status, taxon_id, scientific_name_authorship "
                + "FROM flora.taxon";

        RowMapper<TaxonomyDTO> taxon = (rs, row) -> TaxonomyDTO.builder()
                .scientificName(rs.getString(1))
                .kingdom(rs.getString(2))
                .rank(rs.getString(3))
                .status(rs.getString(4).equalsIgnoreCase("NOME_ACEITO")
                        ? "accept"
                        : (rs.getString(4).equalsIgnoreCase("SINONIMO") ? "synonym" : null))
                .nomeclaturalStatus(rs.getString(5))
                .taxonId(rs.getLong(6))
                .author(rs.getString(7))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<TaxonomyDTO> findTaxonsRanks() {
        String sql = "SELECT taxon_id, taxon_rank FROM flora.taxon";

        RowMapper<TaxonomyDTO> taxon = (rs, row) -> TaxonomyDTO.builder()
                .taxonId(rs.getLong(1))
                .rank(rs.getString(2))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<ParentDTO> findTaxonsParent() {
        String sql = "SELECT taxon_id, parent_name_usage_id FROM flora.taxon order by taxon_id ";

        RowMapper<ParentDTO> taxon = (rs, row) -> ParentDTO.builder()
                .taxonId(rs.getLong(1))
                .parentId(rs.getLong(2))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<AuthorDTO> findTaxonsAuthorship() {
        String sql = "SELECT taxon_id, scientific_name_authorship FROM flora.taxon "
                + "WHERE scientific_name_authorship <> '' ORDER BY scientific_name_authorship";

        RowMapper<AuthorDTO> taxons = (rs, row) -> AuthorDTO.builder()
                .taxonId(rs.getLong(1))
                .authirship(rs.getString(2))
                .build();

        return this.jdbcSecundary.query(sql, taxons);
    }

    public List<SynonymDTO> findTaxonSynonym() {
        String sql = "SELECT relationship_of_resource, related_resource_id, taxon_id FROM flora.resource_relationship";

        RowMapper<SynonymDTO> taxon = (rs, row) -> SynonymDTO.builder()
                .relationship(rs.getString(1))
                .acceptedTxn(rs.getLong(2))
                .taxonId(rs.getLong(3))
                .build();

        return this.jdbcSecundary.query(sql, taxon);
    }

    public List<SpecieProfileDTO> findTaxonsSpecieProfile() {
        String sql = "SELECT habitat, life_form, taxon_id FROM flora.species_profile";

        RowMapper<SpecieProfileDTO> lifes = (rs, row) -> SpecieProfileDTO.builder()
                .habitat(rs.getString(1))
                .lifeForm(rs.getString(2))
                .taxonId(rs.getLong(3))
                .build();

        return this.jdbcSecundary.query(sql, lifes);
    }

    public List<PublicationDTO> findTaxonsPublication() {
        String sql = "SELECT taxon_id, bibliographic_citation, name_published_in, name_published_in_year, \"references\" FROM flora.taxon ";

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
        String sql = "SELECT d.taxon_id, location_id, country_code, establishment_means, d.occurrencere_marks "
                + "FROM flora.distribution d INNER JOIN flora.taxon t ON d.taxon_id = t.taxon_id "
                + "WHERE NOT (trim(location_id) = '' AND trim(country_code) = '' AND trim(establishment_means) = '' AND occurrencere_marks = '{}') ";

        RowMapper<DistributionDTO> distributions = (rs, row) -> DistributionDTO.builder()
                .taxonId(rs.getLong(1))
                .locality(rs.getString(2))
                .countryCode(rs.getString(3))
                .establishmentMeans(rs.getString(4))
                .occurrenceRemarks(rs.getString(5))
                .build();

        return this.jdbcSecundary.query(sql, distributions);
    }

    public List<VernacularDTO> findTaxonsVernacularName() {
        String sql = "SELECT unnest(string_to_array(trim(vernacular_name), ', ')) AS name, language, locality, taxon_id "
                + "FROM flora.vernacular";

        RowMapper<VernacularDTO> vernaculares = (rs, row) -> VernacularDTO.builder()
                .name(rs.getString(1))
                .language(rs.getString(2))
                .locality(rs.getString(3))
                .taxonId(rs.getLong(4))
                .build();

        return this.jdbcSecundary.query(sql, vernaculares);
    }

    public List<TypeSpecimenDTO> findTaxonsTypeSpecimen() {
        String sql = "SELECT catalog_number, collection_code, locality, recorded_by, source, type_status, taxon_id "
                + "FROM flora.type_specimen WHERE NOT (catalog_number = '' AND collection_code = '' AND locality = '' "
                + "AND recorded_by = '' AND source = '' )";

        RowMapper<TypeSpecimenDTO> specimen = (rs, row) -> TypeSpecimenDTO.builder()
                .catalogNumber(rs.getString(1))
                .collectionCode(rs.getString(2))
                .locality(rs.getString(3))
                .recordedBy(rs.getString(4))
                .source(rs.getString(5))
                .typeStatus(rs.getString(6))
                .taxonId(rs.getLong(7))
                .build();

        return this.jdbcSecundary.query(sql, specimen);
    }

    public List<HierarchyDTO> findTaxonsHierarchy() {
        String sql = "SELECT taxon_id, higher_classification, genus, family, classe, phylum, \"order\", kingdom FROM flora.taxon";

        RowMapper<HierarchyDTO> hierarchys = (rs, row) -> HierarchyDTO.builder()
                .taxonId(rs.getLong(1))
                .higherClassification(rs.getString(2))
                .genus(rs.getString(3))
                .family(rs.getString(4))
                .classe(rs.getString(5))
                .phylum(rs.getString(6))
                .order(rs.getString(7))
                .kyngdom(rs.getString(8))
                .build();

        return this.jdbcSecundary.query(sql, hierarchys);
    }

}
