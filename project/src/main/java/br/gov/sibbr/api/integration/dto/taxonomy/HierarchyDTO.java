package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HierarchyDTO {

    private Long taxonId;
    private String kyngdom;
    private String phylum;
    private String classe;
    private String family;
    private String genus;
    private String order;
    private String higherClassification;

    private Long txn;
    private Long parentTxn;
    private Long originalTxn;

    private String name;

    private String type;
    private Long countChildren;

    // Obter dados necessarios para criação arvore complementar
    private Long higherTxn;
    private String rank;
    private Long kingdomId;
    private List<HierarchyComplementaryDTO> complementary;

    // Obter arvore taxonomica
    private String hierarchyFull;

}
