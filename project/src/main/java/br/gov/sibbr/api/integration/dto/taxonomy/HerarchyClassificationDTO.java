package br.gov.sibbr.api.integration.dto.taxonomy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HerarchyClassificationDTO {

    private Long txn;
    private String id;
    private String name;
    private String rank;
}
