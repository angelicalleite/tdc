package br.gov.sibbr.api.integration.dto.complementary;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComplementaryDTO {

    private String id;
    private String name;
    private String url;
    private String acooesConservacao;
    private String alimentacao;
    private String ameacaEstado;
    private String ameacaFatores;
    private String ameacaFauna2008;
    private String ameacaFauna2014;
    private String ameacaJustificativa;
    private String autoria;
    private String citacao;
    private String comportamente;
    private String descricao;
    private String domEpicontinentais;
    private String etimologia;
    private String exemplarReferencias;
    private String experienciaRelevantes;
    private String fonteInformacao;
    private String formadevida;
    private String formadevidaSibbr;
    private String habitat;
    private String importanciaEcologica;
    private String infoDistGeo;
    private String infoHabitat;
    private String infoInvasividade;
    private String insconsisteDist;
    private String insconsistenciasObservadas;
    private String isEndemismo;
    private String isInvasora;
    private String maisInfo;
    private String manejo;
    private String mapaDist;
    private String nomeAceito;
    private String nomeCientifico;
    private String nomePopular;
    private String origem;
    private String planoAcao;
    private String populacao;
    private String presencaUC;
    private String referencia;
    private String referenciaImgEspecie;
    private String referenciaImgMapas;
    private String reino;
    private String reproducao;
    private String resumo;
    private String sinonimo;
    private String usos;

}
