package br.gov.sibbr.api.integration.dao;

import br.gov.sibbr.api.integration.entity.ecology.*;
import br.gov.sibbr.api.integration.entity.taxonomy.Distribuition;
import br.gov.sibbr.api.integration.entity.taxonomy.SpeciesProfile;
import br.gov.sibbr.api.integration.entity.taxonomy.Taxonomy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//todo: remover duplicados assessments
//todo: tratar dados ecologicos, remover numero itens e normalizar nomes com caracteres especiais
//todo: extrair vernacular names de notas
//todo: remover ao final colunas somentes nulos
//todo: checar data ameaça da especie

@Repository
public class EcologyDAO {

    @Autowired
    protected JdbcTemplate jdbcPrimary;

    public List<SpeciesProfile> getSpecieProfiles() {
        String sql = "SELECT id, upper(life_form), upper(habitat), txn FROM taxonomy.specie_profiles ";

        RowMapper<SpeciesProfile> rm = (rs, row) -> SpeciesProfile.builder()
                .id(rs.getLong(1))
                .lifeForm(rs.getString(2))
                .habitat(rs.getString(3))
                .txn(Taxonomy.builder().txn(rs.getLong(4)).build())
                .build();

        return this.jdbcPrimary.query(sql, rm);
    }

    public List<Distribuition> getDistribuitions() {
        String sql = "SELECT id, upper(vegetation_type), endemic_brazil, country_code, "
                + "upper(domain), environment, establishment_means, locality, upper(marine_domain), txn "
                + "FROM taxonomy.distribuition ";

        RowMapper<Distribuition> rm = (rs, row) -> Distribuition.builder()
                .id(rs.getLong(1))
                .vegetationType(rs.getString(2))
                .endemicBrazil(rs.getBoolean(3))
                .countryCode(rs.getString(4))
                .domain(rs.getString(5))
                .environment(rs.getString(6))
                .establishmentMeans(rs.getString(7))
                .locality(rs.getString(8))
                .marineDomain(rs.getString(9))
                .txn(Taxonomy.builder().txn(rs.getLong(10)).build())
                .build();

        return this.jdbcPrimary.query(sql, rm);
    }

    public Set<Habitat> getHabitats(String names) {
        String sql = "SELECT id, name FROM ecology.habitat WHERE name IN (" + names + ")";

        RowMapper<Habitat> rm = (rs, row) -> Habitat.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();

        return new HashSet<>(this.jdbcPrimary.query(sql, rm));
    }

    public Set<LifeForm> getLifeForms(String names) {
        String sql = "SELECT id, name FROM ecology.life_form WHERE name IN (" + names + ")";

        RowMapper<LifeForm> rm = (rs, row) -> LifeForm.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();

        return new HashSet<>(this.jdbcPrimary.query(sql, rm));
    }

    public Set<Biome> getBiomes(String names) {
        String sql = "SELECT id, name FROM ecology.biome WHERE name IN (" + names + ")";

        RowMapper<Biome> rm = (rs, row) -> Biome.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();

        return new HashSet<>(this.jdbcPrimary.query(sql, rm));
    }

    public Set<BiomeMarine> getBiomeMarines(String names) {
        String sql = "SELECT id, name FROM ecology.biome_marine WHERE name IN (" + names + ")";

        RowMapper<BiomeMarine> rm = (rs, row) -> BiomeMarine.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();

        return new HashSet<>(this.jdbcPrimary.query(sql, rm));
    }

    public Set<VegetationType> getVegetationTypes(String names) {
        String sql = "SELECT id, name FROM ecology.vegetation_type WHERE name IN (" + names + ")";

        RowMapper<VegetationType> rm = (rs, row) -> VegetationType.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();

        return new HashSet<>(this.jdbcPrimary.query(sql, rm));
    }

}
