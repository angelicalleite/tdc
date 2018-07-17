package br.gov.sibbr.api.integration.dao;

import br.gov.sibbr.api.integration.dto.complementary.ComplementaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ComplementaryDAO {

    @Autowired
    protected JdbcTemplate jdbcSecundary;


    public List<ComplementaryDTO> findComplementary() {
        String sql = "SELECT * FROM complementary.complementary ";

        RowMapper<ComplementaryDTO> asmt = (rs, row) -> ComplementaryDTO.builder()
                .acooesConservacao(rs.getString(6))
                .alimentacao(rs.getString(6))
                .ameacaEstado(rs.getString(6))
                .ameacaFatores(rs.getString(6))
                .ameacaFauna2008(rs.getString(6))
                .ameacaFauna2014(rs.getString(6))
                .ameacaJustificativa(rs.getString(6))
                .autoria(rs.getString(6))
                .citacao(rs.getString(6))
                .comportamente(rs.getString(6))
                .descricao(rs.getString(6))
                .domEpicontinentais(rs.getString(6))
                .etimologia(rs.getString(6))
                .exemplarReferencias(rs.getString(6))
                .experienciaRelevantes(rs.getString(6))
                .fonteInformacao(rs.getString(6))
                .formadevida(rs.getString(6))
                .formadevidaSibbr(rs.getString(6))
                .habitat(rs.getString(6))
                .id(rs.getString(6))
                .importanciaEcologica(rs.getString(6))
                .infoDistGeo(rs.getString(6))
                .infoHabitat(rs.getString(6))
                .infoInvasividade(rs.getString(6))
                .insconsisteDist(rs.getString(6))
                .insconsistenciasObservadas(rs.getString(6))
                .isEndemismo(rs.getString(6))
                .isInvasora(rs.getString(6))
                .maisInfo(rs.getString(6))
                .manejo(rs.getString(6))
                .mapaDist(rs.getString(6))
                .name(rs.getString(6))
                .nomeAceito(rs.getString(6))
                .nomeCientifico(rs.getString(6))
                .nomePopular(rs.getString(6))
                .origem(rs.getString(6))
                .planoAcao(rs.getString(6))
                .populacao(rs.getString(6))
                .presencaUC(rs.getString(6))
                .referencia(rs.getString(6))
                .referenciaImgEspecie(rs.getString(6))
                .referenciaImgMapas(rs.getString(6))
                .reino(rs.getString(6))
                .reproducao(rs.getString(6))
                .resumo(rs.getString(6))
                .sinonimo(rs.getString(6))
                .build();

        return this.jdbcSecundary.query(sql, asmt);
    }
}
