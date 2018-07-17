package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyComplementaryDTO {

    private Long txn;
    private String id;
    private String name;
    private String rank;
    private List<HerarchyClassificationDTO> classification;

}
