package br.gov.sibbr.api.integration.dao;

import br.gov.sibbr.api.integration.dto.taxonomy.HierarchyDTO;
import br.gov.sibbr.api.integration.dto.taxonomy.TaxonomyDTO;
import br.gov.sibbr.api.integration.entity.taxonomy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaxonomyDAO {

    @Autowired
    protected JdbcTemplate jdbcPrimary;

    public List<TaxonomyDTO> getTxnsByOriginalId(Long originId) {
        try {
            String sql = "SELECT txn, original_txn FROM taxonomy.reference r "
                    + "INNER JOIN taxonomy.taxonomy t ON r.reference_txn = t.txn "
                    + "WHERE origin_id = ? ";

            RowMapper<TaxonomyDTO> txn = (rs, row) -> TaxonomyDTO.builder()
                    .txn(rs.getLong(1))
                    .originalTxn(rs.getLong(2))
                    .build();

            return this.jdbcPrimary.query(sql, txn, originId);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Taxonomy getTxnByOriginalTxn(Long originalTxn, Long originId) {
        try {
            String sql = "SELECT t.txn, t.parent_txn, t.scientific_name, t.status, t.rank_id, t.author_id, kingdom_id, nomeclatural_status "
                    + "FROM taxonomy.reference r INNER JOIN taxonomy.taxonomy t ON r.reference_txn=t.txn "
                    + "WHERE r.original_txn = ? AND r.origin_id = ? ";

            RowMapper<Taxonomy> txn = (rs, row) -> Taxonomy.builder()
                    .txn(rs.getLong(1))
                    .parentTxn(rs.getLong(2))
                    .scientificName(rs.getString(3))
                    .status(rs.getString(4))
                    .rank(rs.getLong(5) == 0 ? null : Rank.builder().id(rs.getLong(5)).build())
                    .author(rs.getLong(6) == 0 ? null : Author.builder().id(rs.getLong(6)).build())
                    .kingdom(rs.getLong(7) == 0 ? null : Kingdom.builder().id(rs.getLong(7)).build())
                    .nomeclaturalStatus(rs.getString(8))
                    .build();

            return this.jdbcPrimary.queryForObject(sql, txn, originalTxn, originId);

        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public List<Semantic> findTxnNotSemantics(String status) {
        String sql = "SELECT txn, scientific_name, status, kingdom_id FROM taxonomy.taxonomy t "
                + "WHERE status = ? AND NOT EXISTS(SELECT txn FROM taxonomy.semantic l WHERE l.txn = t.txn )";

        RowMapper<Semantic> txn = (rs, row) -> Semantic.builder()
                .txn(Taxonomy.builder().txn(rs.getLong(1)).build())
                .scientificName(rs.getString(2))
                .status(rs.getString(3))
                .build();

        return this.jdbcPrimary.query(sql, txn, status);
    }

    public List<HierarchyDTO> findTxnNotHierarchy(String type, Long originId) {
        String sql = "SELECT txn, parent_txn, original_txn FROM taxonomy.taxonomy t INNER JOIN taxonomy.reference r ON t.txn = r.reference_txn "
                + "WHERE r.origin_id = ? AND NOT EXISTS(SELECT txn FROM taxonomy.hierarchy l WHERE l.txn = t.txn AND l.type = ? )";

        RowMapper<HierarchyDTO> txn = (rs, row) -> HierarchyDTO.builder()
                .txn(rs.getLong(1))
                .parentTxn(rs.getLong(2) != 0 ? rs.getLong(2) : null)
                .originalTxn(rs.getLong(3))
                .build();

        return this.jdbcPrimary.query(sql, txn, originId, type);
    }

    public List<Taxonomy> findTxnTypeHierarchy() {
        String sql = "SELECT txn, parent_txn FROM taxonomy.taxonomy t "
                + "WHERE NOT EXISTS(SELECT txn FROM taxonomy.hierarchy l WHERE l.txn = t.txn AND type = 'hierarchy' )";

        RowMapper<Taxonomy> taxons = (rs, row) -> Taxonomy.builder()
                .txn(rs.getLong(1))
                .parentTxn(rs.getLong(2) == 0 ? null : rs.getLong(2))
                .build();

        return this.jdbcPrimary.query(sql, taxons);
    }

    public List<HierarchyDTO> findHierarchyByHigherTxn() {
        String sql = "SELECT higher_txn, canonical_name, r.name AS rank, kingdom_id "
                + "FROM taxonomy.hierarchy h, taxonomy.semantic l , taxonomy.taxonomy t, taxonomy.rank r "
                + "WHERE h.higher_txn = t.txn AND t.rank_id=r.id AND count_children > 0 "
                + "AND h.higher_txn = l.txn AND h.type = 'hierarchy' AND higher_txn NOTNULL "
                + "GROUP BY higher_txn, canonical_name, r.name, kingdom_id ORDER BY canonical_name";

        RowMapper<HierarchyDTO> hierarchys = (rs, row) -> HierarchyDTO.builder()
                .higherTxn(rs.getLong(1))
                .name(rs.getString(2))
                .rank(rs.getString(3))
                .kingdomId(rs.getLong(4))
                .build();

        return this.jdbcPrimary.query(sql, hierarchys);
    }

    public List<Hierarchy> findHierarchiesComplementarysNotFull() {
        String sql = "SELECT h1.txn, h1.parent_txn, h1.higher_txn, h1.type, h1.hierarchy, h1.count_children, h1.level "
                + "FROM taxonomy.hierarchy h1 WHERE type = 'complementary' AND NOT EXISTS(SELECT txn FROM taxonomy.hierarchy h2  "
                + "WHERE h1.txn = h2.txn AND type = 'full') ";

        RowMapper<Hierarchy> hierarchys = (rs, row) -> Hierarchy.builder()
                .txn(Taxonomy.builder().txn(rs.getLong(1)).build())
                .parentTxn(rs.getLong(2))
                .higherTxn(rs.getLong(3))
                .hierarchy(rs.getString(5))
                .countChildren(rs.getLong(6))
                .level(rs.getInt(7))
                .build();

        return this.jdbcPrimary.query(sql, hierarchys);
    }

    public List<HierarchyDTO> findHierarchyFullByTxns(String txns) {
        String sql = "SELECT t.txn, l.canonical_name, r.name, t.kingdom_id "
                + "FROM taxonomy.taxonomy t, taxonomy.semantic l, taxonomy.rank r "
                + "WHERE t.txn IN (?) AND t.txn = l.txn AND t.rank_id = r.id ";

        RowMapper<HierarchyDTO> hierarchys = (rs, row) -> HierarchyDTO.builder()
                .txn(rs.getLong(1))
                .hierarchyFull(rs.getString(2) + ":" + rs.getString(3) + ":" + rs.getLong(4))
                .build();

        return jdbcPrimary.query(sql, hierarchys, txns);
    }

    public List<TaxonomyDTO> findTaxonomyDuplicateScientificName() {
        String sql = "SELECT txn FROM taxonomy.taxonomy t WHERE scientific_name IN ( SELECT scientific_name "
                + "FROM taxonomy.taxonomy t GROUP BY scientific_name HAVING count(scientific_name) > 1) ";

        RowMapper<TaxonomyDTO> txns = (rs, row) -> TaxonomyDTO.builder()
                .txn(rs.getLong(1))
                .build();

        return jdbcPrimary.query(sql, txns);
    }

    public List<TaxonomyDTO> findTaxonomyRankIsNull() {
        String sql = "SELECT txn FROM taxonomy.taxonomy t WHERE rank_id ISNULL OR rank_id = 0 ";

        RowMapper<TaxonomyDTO> txns = (rs, row) -> TaxonomyDTO.builder()
                .txn(rs.getLong(1))
                .build();

        return jdbcPrimary.query(sql, txns);
    }

    public List<TaxonomyDTO> findTaxonomyStatusIsNull() {
        String sql = "SELECT txn FROM taxonomy.taxonomy t WHERE status ISNULL OR trim(status) = '' ";

        RowMapper<TaxonomyDTO> txns = (rs, row) -> TaxonomyDTO.builder()
                .txn(rs.getLong(1))
                .build();

        return jdbcPrimary.query(sql, txns);
    }

    public List<TaxonomyDTO> findTaxonomyScientificNameContains(String expression) {
        String sql = "SELECT txn FROM taxonomy.taxonomy t WHERE scientific_name LIKE ? ";

        RowMapper<TaxonomyDTO> txns = (rs, row) -> TaxonomyDTO.builder()
                .txn(rs.getLong(1))
                .build();

        return jdbcPrimary.query(sql, txns, "%" + expression + "%");
    }

    public Taxonomy getTxnBySemantics(String scientificName, String canonicalName) {
        try {
            String sql = "SELECT txn FROM taxonomy.semantic WHERE canonical_name = ? OR  scientific_name = ? ";

            RowMapper<Taxonomy> txn = (rs, row) -> Taxonomy.builder()
                    .txn(rs.getLong(1))
                    .build();

            return this.jdbcPrimary.queryForObject(sql, txn, canonicalName, scientificName);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Long countByParentTxn(Long txn) {
        try {
            String sql = "SELECT count(*) FROM taxonomy.taxonomy WHERE parent_txn = ? ";

            RowMapper<Long> count = (rs, row) -> rs.getLong(1);

            return this.jdbcPrimary.queryForObject(sql, count, txn);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Author getAuthor(String name) {
        try {
            String sql = "SELECT id, name FROM taxonomy.author WHERE name = ? ";

            RowMapper<Author> author = (rs, row) -> Author.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build();

            return this.jdbcPrimary.queryForObject(sql, author, name);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Rank getRankByNameOrSimilar(String name) {
        try {
            String sql = "SELECT  id, name FROM taxonomy.rank "
                    + "WHERE lower(name) = ? OR ? ILIKE ANY (string_to_array(similar_name, ', '))";

            RowMapper<Rank> rank = (rs, row) -> Rank.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build();

            return this.jdbcPrimary.queryForObject(sql, rank, name.toLowerCase(), name);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public Kingdom getKingdomByNameOrSimilar(String name) {
        try {
            String sql = "SELECT id, name FROM taxonomy.kingdom "
                    + "WHERE lower(name) = ? OR ? ILIKE ANY (string_to_array(similar_name, ', '))";

            RowMapper<Kingdom> kingdom = (rs, row) -> Kingdom.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build();

            return this.jdbcPrimary.queryForObject(sql, kingdom, name.toLowerCase(), name);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

    public List<HierarchyDTO> getHierarchy() {
        try {
            String sql = "WITH RECURSIVE hierarchy AS "
                    + "(SELECT t.txn, CAST(t.txn AS TEXT) AS full "
                    + "  FROM taxonomy.taxonomy t WHERE t.parent_txn IS NULL "
                    + "  UNION ALL "
                    + "  SELECT t.txn, CAST(hierarchy.full || ' | ' || t.txn AS TEXT) AS full "
                    + "  FROM taxonomy.taxonomy t INNER JOIN hierarchy ON t.parent_txn = hierarchy.txn) "
                    + "SELECT hierarchy.txn, hierarchy.full "
                    + "FROM hierarchy ORDER BY hierarchy.txn ";

            RowMapper<HierarchyDTO> hierarchy = (rs, row) -> HierarchyDTO.builder()
                    .txn(rs.getLong(1))
                    .hierarchyFull(rs.getString(2))
                    .build();

            return this.jdbcPrimary.query(sql, hierarchy);
        } catch (IncorrectResultSizeDataAccessException ex) {
            return null;
        }
    }

}
